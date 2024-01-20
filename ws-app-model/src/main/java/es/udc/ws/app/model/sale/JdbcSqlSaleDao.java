package es.udc.ws.app.model.sale;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcSqlSaleDao extends AbstractSqlSaleDao {
  @Override
  public Sale create(Connection connection, Sale sale) {

    String queryString = "INSERT INTO Sale"
        + " (partidoId, userMail, creditCardNumber, saleDate, amount, delivered)"
        + " VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
        Statement.RETURN_GENERATED_KEYS)) {
      int i = 1;
      preparedStatement.setLong(i++, sale.getPartidoId());
      preparedStatement.setString(i++, sale.getUserMail());
      preparedStatement.setString(i++, sale.getCreditCardNumber());
      preparedStatement.setTimestamp(i++, Timestamp.valueOf(sale.getSaleDate()));
      preparedStatement.setInt(i++, sale.getAmount());
      preparedStatement.setBoolean(i++, sale.getDelivered());

      preparedStatement.executeUpdate();

      ResultSet resultSet = preparedStatement.getGeneratedKeys();

      if(!resultSet.next()) {
        throw new SQLException("JDBC driver did not return generated key.");
      }
      Long saleId = resultSet.getLong(1);

      return new Sale(saleId, sale.getPartidoId(), sale.getUserMail(), sale.getCreditCardNumber(), sale.getSaleDate(),
          sale.getAmount(), sale.getDelivered());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
