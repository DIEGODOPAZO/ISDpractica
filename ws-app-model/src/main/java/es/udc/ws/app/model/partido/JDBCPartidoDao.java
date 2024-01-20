package es.udc.ws.app.model.partido;

import java.sql.*;

public class JDBCPartidoDao extends AbstractSQLPartidoDao{
    @Override
    public Partido create(Connection connection, Partido partido) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Partido"
                + " (price, datep, creationDate, rival, entradasVendidas, entradasIniciales)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setFloat(i++, partido.getPrice());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getDate()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(partido.getCreationDate()));
            preparedStatement.setString(i++, partido.getRival());
            preparedStatement.setInt(i++, partido.getEntradasVendidas());
            preparedStatement.setInt(i++, partido.getEntradasIniciales());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long partidoId = resultSet.getLong(1);

            /* Return movie. */
            return new Partido(partidoId, partido.getPrice(), partido.getDate(),
                    partido.getRival(), partido.getEntradasVendidas(), partido.getEntradasIniciales(),
                    partido.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
