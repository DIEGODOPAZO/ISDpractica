package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SQLPartidoDao;
import es.udc.ws.app.model.partido.SQLPartidoDaoFactory;
import es.udc.ws.app.model.partidoservice.PartidoService;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.model.partidoservice.exceptions.*;
import es.udc.ws.app.model.sale.JdbcSqlSaleDao;
import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.app.model.sale.SqlSaleDao;
import es.udc.ws.app.model.sale.SqlSaleDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private static PartidoService partidoService = null;
    private static SqlSaleDao saleDao = null;
    private static SQLPartidoDao partidoDao = null;

    private final String USER_MAIL = "ws-user@example.com";
    private final String INVALID_USER_MAIL1 = "ws-user";
    private final String INVALID_USER_MAIL2 = "ws-user@jwhgf";
    private final String INVALID_USER_MAIL3 = "@jwhgf";
    private final String INVALID_USER_MAIL4 = "@example.com";
    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private final long NON_EXISTENT_PARTIDO_ID = -1;
    private final long NON_EXISTENT_SALE_ID = -1;
    @BeforeAll
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this)
         * is needed to test "es.udc.ws.movies.model.partidoService.PartidoService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        partidoService = PartidoServiceFactory.getService();

        saleDao = SqlSaleDaoFactory.getDao();

        partidoDao = SQLPartidoDaoFactory.getDao();

    }

    private Partido getValidPartido() {
        return new Partido(19.95F, LocalDateTime.now().withNano(0).plusDays(1), "Rival", 220, 300);
    }

    private Partido getExpiredPartido() {
        return new Partido(29.99F, LocalDateTime.now().minusDays(7), "Rival2", 100, 500);
    }

    private Partido getValidPartido(LocalDateTime datep){
        return new Partido(19.95F, datep, "Rival", 220, 300);
    }
    //private Partido getValidPartido() {return getValidPartido(1598);}

    private Partido createPartido(Partido partido) {

        Partido addedPartido = null;
        try {
            addedPartido = partidoService.addPartido(partido);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedPartido;

    }

    private void removePartido(Long partidoId) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                try {
                    partidoDao.remove(connection, partidoId);
                } catch (InstanceNotFoundException e) {
                    throw new PartidoNotRemovableException(partidoId);
                }

                /* Commit. */
                connection.commit();

            } catch (PartidoNotRemovableException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeSale(Long saleId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                saleDao.remove(connection, saleId);
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private Sale findSale (Long saleId) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try(Connection connection = dataSource.getConnection()) {

            return saleDao.find(connection, saleId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddPartidoAndFindPartido() throws InputValidationException, InstanceNotFoundException {

        Partido partido = getValidPartido();
        Partido addedPartido = null;

        try {
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
            addedPartido = partidoService.addPartido(partido);
            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Partido foundPartido = partidoService.findPartidoById(addedPartido.getPartidoId());

            assertEquals(addedPartido, foundPartido);
            assertEquals(addedPartido.getPartidoId(), foundPartido.getPartidoId());
            assertEquals(partido.getDate(), foundPartido.getDate());
            assertTrue((!foundPartido.getCreationDate().isBefore(beforeCreationDate))
                    && (!foundPartido.getCreationDate().isAfter(afterCreationDate)));
            assertEquals(partido.getRival(), foundPartido.getRival());
            assertEquals(partido.getPrice(), foundPartido.getPrice());
            assertEquals(partido.getEntradasVendidas(),foundPartido.getEntradasVendidas());
        } finally {
            if(addedPartido!=null) {
                removePartido(addedPartido.getPartidoId());
            }
        }
    }

    @Test
    public void testFindNonExistentPartido() {
        assertThrows(InstanceNotFoundException.class, () -> partidoService.findPartidoById(NON_EXISTENT_PARTIDO_ID));
    }


    @Test
    public void testAddInvalidPartidos() {

        // Comprobar precio de las entradas
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setPrice(-1.6F);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });

        // Mirar si el partido tiene un rival
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setRival(null);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });

        // Mirar si el partido tiene un rival
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setRival("");
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });

        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasVendidas(-9);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });

        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasVendidas(500);
            Partido addedPartido = partidoService.addPartido(partido);
            removePartido(addedPartido.getPartidoId());
        });
    }

    @Test
    public void testFindByDates(){
        List<Partido> partidos = new LinkedList<Partido>();
        LocalDateTime fechaActual = LocalDateTime.now();
        int year = fechaActual.getYear();

        //se crean los partidos
        LocalDateTime firstDate = LocalDateTime.of(year + 1, 10, 10, 15, 30, 0).withNano(0);
        LocalDateTime secondDate = LocalDateTime.of(year + 1, 10, 15, 15, 30, 0).withNano(0);
        LocalDateTime thirdDate = LocalDateTime.of(year + 1, 10, 20, 15, 30, 0).withNano(0);
        LocalDateTime fourthDate = LocalDateTime.of(year + 1, 10, 25, 15, 30, 0).withNano(0);
        //se crea un rango inexistente
        LocalDateTime nonExistRange1 = LocalDateTime.of(year + 1, 11, 25, 15, 30, 0).withNano(0);
        LocalDateTime nonExistRange2 = LocalDateTime.of(year + 1, 11, 30, 15, 30, 0).withNano(0);
        // crear partidos con diferentes fechas
        Partido partido1 = createPartido(getValidPartido(firstDate));
        partidos.add(partido1);
        Partido partido2 = createPartido(getValidPartido(secondDate));
        partidos.add(partido2);
        Partido partido3 = createPartido(getValidPartido(thirdDate));
        partidos.add(partido3);
        Partido partido4 = createPartido(getValidPartido(fourthDate));
        partidos.add(partido4);

        try {
            List<Partido> foundPartidos = partidoService.findPartidoByDates(firstDate.minusMonths(1), secondDate.plusMonths(1));
            assertEquals(partidos, foundPartidos);

            foundPartidos = partidoService.findPartidoByDates(firstDate, firstDate);
            assertEquals(1, foundPartidos.size());
            assertEquals(partidos.get(0), foundPartidos.get(0));

            foundPartidos = partidoService.findPartidoByDates(secondDate, secondDate);
            assertEquals(1, foundPartidos.size());
            assertEquals(partidos.get(1), foundPartidos.get(0));

            foundPartidos = partidoService.findPartidoByDates(nonExistRange1, nonExistRange2);
            assertEquals(0, foundPartidos.size());
        } finally {
            // Clear Database
            for (Partido partido : partidos) {
                removePartido(partido.getPartidoId());
            }
        }
    }

    @Test
    public void testBuyAndFindSale ()
            throws InstanceNotFoundException, PartidoCelebradoException, EntradasInsuficientesException, InputValidationException {
        Partido partido = createPartido(getValidPartido());
        Sale sale = null;

        try {
            int entradasVendidasAntes = partido.getEntradasVendidas();

            LocalDateTime beforeBuyDate = LocalDateTime.now().withNano(0);

            sale = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, VALID_CREDIT_CARD_NUMBER, 5);

            LocalDateTime afterBuyDate = LocalDateTime.now().withNano(0);

            Sale foundSale = findSale(sale.getSaleID());

            Partido foundPartido = partidoService.findPartidoById(partido.getPartidoId());

            assertEquals(sale, foundSale);;
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundSale.getCreditCardNumber());
            assertEquals(USER_MAIL, foundSale.getUserMail());
            assertEquals(foundPartido.getPartidoId(), foundSale.getPartidoId());
            assertEquals(entradasVendidasAntes + 5, foundPartido.getEntradasVendidas());
            assertTrue((!foundSale.getSaleDate().isBefore(beforeBuyDate))
                    && (!foundSale.getSaleDate().isAfter(afterBuyDate)));
        } finally {
            if(sale != null) {
                removeSale(sale.getSaleID());
            }
            removePartido(partido.getPartidoId());
        }
    }
    @Test
    public void testBuyEntradasWithInvalidCard() {
        Partido partido = createPartido(getValidPartido());
        try{
            assertThrows(InputValidationException.class, () -> {
                Sale sale = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, INVALID_CREDIT_CARD_NUMBER, 10);
                removeSale(sale.getSaleID());
            });
        } finally {
            removePartido(partido.getPartidoId());
        }
    }

    @Test
    public void testBuyEntradasWithInvalidMail() {
        Partido partido = createPartido(getValidPartido());
        try{
            assertThrows(InputValidationException.class, () -> {
                Sale sale = partidoService.buyEntradas(partido.getPartidoId(), INVALID_USER_MAIL1, VALID_CREDIT_CARD_NUMBER, 10);
                removeSale(sale.getSaleID());
            });
            assertThrows(InputValidationException.class, () -> {
                Sale sale = partidoService.buyEntradas(partido.getPartidoId(), INVALID_USER_MAIL2, VALID_CREDIT_CARD_NUMBER, 10);
                removeSale(sale.getSaleID());
            });
            assertThrows(InputValidationException.class, () -> {
                Sale sale = partidoService.buyEntradas(partido.getPartidoId(), INVALID_USER_MAIL3, VALID_CREDIT_CARD_NUMBER, 10);
                removeSale(sale.getSaleID());
            });
            assertThrows(InputValidationException.class, () -> {
                Sale sale = partidoService.buyEntradas(partido.getPartidoId(), INVALID_USER_MAIL4, VALID_CREDIT_CARD_NUMBER, 10);
                removeSale(sale.getSaleID());
            });
        } finally {
            removePartido(partido.getPartidoId());
        }
    }

    @Test
    public void testBuyNonExistentPartido() {
        assertThrows(InstanceNotFoundException.class, () -> {
            Sale sale = partidoService.buyEntradas(NON_EXISTENT_PARTIDO_ID, USER_MAIL, VALID_CREDIT_CARD_NUMBER, 8);
            removeSale(sale.getSaleID());
        });
    }

     @Test
    public void testGetSalesByUser() throws EntradasInsuficientesException, PartidoCelebradoException{
        Partido partido = createPartido(getValidPartido());
        Sale sale1 = null;
        Sale sale2 = null;

        try {
            // Comprar dos ventas para el mismo usuario
            sale1 = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, VALID_CREDIT_CARD_NUMBER, 3);
            sale2 = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, VALID_CREDIT_CARD_NUMBER, 2);

            List<Sale> userSales = partidoService.findUserSales(USER_MAIL);

            assertEquals(2, userSales.size());

            //Comprobacion de la primera venta
            Sale foundSale1 = userSales.get(0);
            assertEquals(sale1.getSaleID(), foundSale1.getSaleID());
            assertEquals(USER_MAIL, foundSale1.getUserMail());
            assertEquals(partido.getPartidoId(), foundSale1.getPartidoId());

            //Comprobacion de la segunda venta
            Sale foundSale2 = userSales.get(1);
            assertEquals(sale2.getSaleID(), foundSale2.getSaleID());
            assertEquals(USER_MAIL, foundSale2.getUserMail());
            assertEquals(partido.getPartidoId(), foundSale2.getPartidoId());

        } catch (InstanceNotFoundException | InputValidationException e) {
            throw new RuntimeException(e);
        }finally {
            // Limpiar la base de datos
            if (sale1 != null) {
                removeSale(sale1.getSaleID());
            }
            if (sale2 != null) {
                removeSale(sale2.getSaleID());
            }
            removePartido(partido.getPartidoId());
        }
    }

    @Test
    public void errorFindUserSales() throws InputValidationException {
        assertThrows(InputValidationException.class, () -> {
            partidoService.findUserSales(INVALID_USER_MAIL1);
        });

    }

    @Test
    public void testDeliverTicket() throws PartidoCelebradoException, EntradasInsuficientesException, InstanceNotFoundException, InputValidationException {
        Partido partido = createPartido(getValidPartido());
        Sale sale = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, VALID_CREDIT_CARD_NUMBER, 3);
        Sale sale2 = partidoService.buyEntradas(partido.getPartidoId(), USER_MAIL, VALID_CREDIT_CARD_NUMBER, 3);

        try {

            // Almacenar el ID de la venta en una variable final
            final long saleId = sale.getSaleID();
            final long saleId2 = sale2.getSaleID();

            try {
                // Intentar entregar entradas con una tarjeta incorrecta
                assertThrows(InputValidationException.class, () -> {
                    partidoService.deliverTicket(saleId, "TarjetaIncorrecta");
                });

                assertThrows(InputValidationException.class, () -> {
                    partidoService.deliverTicket(saleId, "TarjetaIncorrecta");
                });

                // Intentar entregar entradas nuevamente
                assertThrows(AlreadyCollectedException.class, () -> {
                    partidoService.deliverTicket(saleId, VALID_CREDIT_CARD_NUMBER);
                    partidoService.deliverTicket(saleId, VALID_CREDIT_CARD_NUMBER);
                });

            } finally {

            }
        } finally {
            // Limpiar la base de datos
            cleanUpTestData(sale, sale2, partido);
        }
    }

    private void cleanUpTestData(Sale sale1, Sale sale2, Partido partido) {
        try {
            if (sale1 != null) {
                removeSale(sale1.getSaleID());
            }
            if (sale2 != null) {
                removeSale(sale2.getSaleID());
            }
            if (partido != null) {
                removePartido(partido.getPartidoId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
