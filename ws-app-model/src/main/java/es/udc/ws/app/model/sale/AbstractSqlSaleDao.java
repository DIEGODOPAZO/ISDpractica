package es.udc.ws.app.model.sale;

import es.udc.ws.app.model.partidoservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.partidoservice.exceptions.InvalidCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSqlSaleDao implements SqlSaleDao {
  protected AbstractSqlSaleDao () {}

  @Override
  public Sale find(Connection connection, Long saleId) throws InstanceNotFoundException {

      String queryString = "SELECT partidoId, userMail, creditCardNumber, saleDate, amount, delivered"
          + " FROM Sale WHERE saleId = ?";

      try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
        int i = 1;
        preparedStatement.setLong(i++, saleId);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
          throw new InstanceNotFoundException(saleId, Sale.class.getName());
        }

        i = 1;
        Long partidoId = resultSet.getLong(i++);
        String userMail = resultSet.getString(i++);
        String creditCardNumber = resultSet.getString(i++);
        Timestamp saleDateAsTimestamp = resultSet.getTimestamp(i++);
        LocalDateTime saleDate = saleDateAsTimestamp.toLocalDateTime();
        int amount = resultSet.getInt(i++);
        boolean delivered = resultSet.getBoolean(i++);

        return new Sale(saleId, partidoId, userMail, creditCardNumber, saleDate, amount, delivered);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
  }

  @Override
  public void update(Connection connection, Sale sale) throws InstanceNotFoundException {
    String queryString ="UPDATE Sale SET partidoId = ?, userMail = ?, saleDate = ?, creditCardNumber = ?"
        + ", amount = ?, delivered = ?  WHERE saleId = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
      int i = 1;
      preparedStatement.setLong(i++, sale.getPartidoId());
      preparedStatement.setString(i++, sale.getUserMail());
      preparedStatement.setTimestamp(i++, Timestamp.valueOf(sale.getSaleDate()));
      preparedStatement.setString(i++, sale.getCreditCardNumber());
      preparedStatement.setLong(i++, sale.getAmount());
      preparedStatement.setBoolean(i++, sale.getDelivered());
      preparedStatement.setLong(i++, sale.getSaleID());


      int updatedRows = preparedStatement.executeUpdate();

      if (updatedRows == 0) {
        throw new InstanceNotFoundException(sale.getPartidoId(), Sale.class.getName());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(Connection connection, Long saleId) throws InstanceNotFoundException {
    String queryString = "DELETE FROM Sale WHERE" + " saleId = ?";

    try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
      int i = 1;
      preparedStatement.setLong(i++, saleId);

      int removeRows = preparedStatement.executeUpdate();

      if (removeRows == 0) {
        throw new InstanceNotFoundException(saleId, Sale.class.getName());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Sale> findByUserMail(Connection connection, String userMail) throws InputValidationException {
    String queryString = "SELECT saleId, partidoId, creditCardNumber, saleDate, amount, delivered"
            + " FROM Sale WHERE userMail = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
      int i = 1;
      preparedStatement.setString(i++, userMail);

      ResultSet resultSet = preparedStatement.executeQuery();

      List<Sale> sales = new ArrayList<>();
      List<Sale> emptySales = new ArrayList<>();

      boolean found = false;

      while (resultSet.next()) {
        found = true;
        i = 1;
        Long saleId = resultSet.getLong(i++);
        Long partidoId = resultSet.getLong(i++);
        String creditCardNumber = resultSet.getString(i++);
        Timestamp saleDateAsTimestamp = resultSet.getTimestamp(i++);
        LocalDateTime saleDate = saleDateAsTimestamp.toLocalDateTime();
        int amount = resultSet.getInt(i++);
        boolean delivered = resultSet.getBoolean(i++);

        sales.add(new Sale(saleId, partidoId, userMail, creditCardNumber, saleDate, amount, delivered));
      }

      // Si no se encontró ninguna venta, lanzar la excepción
      if (!found) {
        return emptySales;
      }


      return sales;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Sale findByMailCard(Connection connection, Long saleId, String creditCardNumber) throws InvalidCardException{
      String queryString = "SELECT partidoId, userMail, saleDate, amount, delivered"
              + " FROM Sale WHERE saleId = ? AND creditCardNumber = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
      int i = 1;
      preparedStatement.setLong(i++, saleId);
      preparedStatement.setString(i++, creditCardNumber);

      ResultSet resultSet = preparedStatement.executeQuery();

      if (!resultSet.next()) {
        throw new InvalidCardException(saleId, creditCardNumber);
      }

      i = 1;
      Long partidoId = resultSet.getLong(i++);
      String userMail = resultSet.getString(i++);
      Timestamp saleDateAsTimestamp = resultSet.getTimestamp(i++);
      LocalDateTime saleDate = saleDateAsTimestamp.toLocalDateTime();
      int amount = resultSet.getInt(i++);
      boolean delivered = resultSet.getBoolean(i++);

      return new Sale(saleId, partidoId, userMail, creditCardNumber, saleDate, amount, delivered);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateDeliver(Connection connection, Sale sale, String creditCardNumber) throws InstanceNotFoundException, AlreadyCollectedException, InvalidCardException {

    // If the delivered field is set to false, execute the update query
    String queryString = "UPDATE Sale SET partidoId = ?, userMail = ?, saleDate = ?, creditCardNumber = ?,"
            + " amount = ?, delivered = ? WHERE saleId = ? AND delivered = false";

    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

      if (!sale.getCreditCardNumber().equals(creditCardNumber)) {
        throw new InvalidCardException(sale.getSaleID(), creditCardNumber);
      }

      if (sale.getDelivered()) {
        throw new AlreadyCollectedException(sale.getPartidoId(), sale.getCreditCardNumber());
      }
      
      int i = 1;
      preparedStatement.setLong(i++, sale.getPartidoId());
      preparedStatement.setString(i++, sale.getUserMail());
      preparedStatement.setTimestamp(i++, Timestamp.valueOf(sale.getSaleDate()));
      preparedStatement.setString(i++, sale.getCreditCardNumber());
      preparedStatement.setLong(i++, sale.getAmount());
      preparedStatement.setBoolean(i++, true);
      preparedStatement.setLong(i++, sale.getSaleID());

      /* Execute query. */
      int updatedRows = preparedStatement.executeUpdate();

      if (updatedRows == 0) {
        throw new InstanceNotFoundException(sale.getPartidoId(), sale.getCreditCardNumber());
      }

      // Check if the sale was not delivered and not updated

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
