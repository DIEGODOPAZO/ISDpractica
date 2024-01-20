package es.udc.ws.app.model.partidoservice;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SQLPartidoDao;
import es.udc.ws.app.model.partido.SQLPartidoDaoFactory;
import es.udc.ws.app.model.partidoservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.partidoservice.exceptions.EntradasInsuficientesException;
import es.udc.ws.app.model.partidoservice.exceptions.InvalidCardException;
import es.udc.ws.app.model.partidoservice.exceptions.PartidoCelebradoException;
import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.app.model.sale.SqlSaleDao;
import es.udc.ws.app.model.sale.SqlSaleDaoFactory;
import es.udc.ws.app.model.util.PropertyValidator;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

public class PartidoServiceImpl implements PartidoService{

    private final DataSource dataSource;
    private SQLPartidoDao partidoDao = null;
    private SqlSaleDao saleDao = null;

    public PartidoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        partidoDao = SQLPartidoDaoFactory.getDao();
        saleDao = SqlSaleDaoFactory.getDao();
    }

    private void validatePartido(Partido partido) throws InputValidationException {
        PropertyValidator.validateEntradasIniciales(partido.getEntradasIniciales());
        PropertyValidator.validateDate(partido.getDate());
        PropertyValidator.validateDateCreation(partido.getDate(), partido.getCreationDate());
        PropertyValidator.validatePrice(partido.getPrice());
        PropertyValidator.validateRival(partido.getRival());
        PropertyValidator.validateEntradasVendidas(partido.getEntradasVendidas(), partido.getEntradasIniciales());
    }
    @Override
    public Partido addPartido(Partido partido) throws InputValidationException {
        validatePartido(partido);
        partido.setCreationDate(LocalDateTime.now().withNano(0));

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Partido createdPartido = partidoDao.create(connection, partido);

                /* Commit. */
                connection.commit();

                return createdPartido;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Partido findPartidoById(Long partidoId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return partidoDao.findById(connection, partidoId);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partido> findPartidoByDates(LocalDateTime date1, LocalDateTime date2){
        try (Connection connection = dataSource.getConnection()) {
            return partidoDao.findByDate(connection, date1, date2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sale buyEntradas(Long partidoId, String userMail, String creditCardNumber, int amount)
        throws InstanceNotFoundException, InputValidationException, EntradasInsuficientesException, PartidoCelebradoException {
        PropertyValidator.validateCreditCard(creditCardNumber);
        PropertyValidator.validateUserMail(userMail);
        PropertyValidator.validateMayor0(amount);
        try (Connection connection = dataSource.getConnection()){
            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Partido partido = partidoDao.findById(connection, partidoId);
                PropertyValidator.validateAmount(amount, partido);
                partido.addEntradasVendidas(amount);
                partidoDao.update(connection, partido);
                LocalDateTime saleDate = LocalDateTime.now().withNano(0);
                PropertyValidator.validateSaleDate(saleDate, partido);
                Sale sale = saleDao.create(connection, new Sale(partidoId, userMail, creditCardNumber,
                    saleDate, amount, false));

                connection.commit();

                return sale;

            } catch (EntradasInsuficientesException | InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (RuntimeException | Error | PartidoCelebradoException e) {
                connection.rollback();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     @Override
    public List<Sale> findUserSales(String userMail) throws InputValidationException {
        PropertyValidator.validateUserMail(userMail);
        try (Connection connection = dataSource.getConnection()) {
            return saleDao.findByUserMail(connection, userMail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deliverTicket(Long saleId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, AlreadyCollectedException, InvalidCardException {

        PropertyValidator.validateCreditCard(creditCardNumber);


        try (Connection connection = dataSource.getConnection()){
            Sale sale = saleDao.find(connection, saleId);
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                saleDao.updateDeliver(connection, sale, creditCardNumber);

                if (!sale.getCreditCardNumber().equals(creditCardNumber))
                    throw new InvalidCardException(sale.getSaleID(), creditCardNumber);

                connection.commit();

            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


   /* @Override
    public void markSaleAsCollected(Sale sale)
            throws InstanceNotFoundException, InvalidCardException, AlreadyCollectedException {

        try (Connection connection = dataSource.getConnection()) {

            saleDao.updateDeliver(connection, sale);

        } catch (AlreadyCollectedException e) {
            throw new AlreadyCollectedException(sale.getPartidoId(), sale.getCreditCardNumber());
        }catch (SQLException e) {
            throw new RuntimeException("Error executing SQL update", e);
        }
    }*/


}
