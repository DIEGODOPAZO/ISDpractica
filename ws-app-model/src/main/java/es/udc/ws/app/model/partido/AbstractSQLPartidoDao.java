package es.udc.ws.app.model.partido;



import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSQLPartidoDao implements SQLPartidoDao {
    @Override
    public Partido findById(Connection connection, Long partidoId) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT rival, "
            + " entradasVendidas, entradasIniciales, price, creationDate, datep FROM Partido WHERE partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, partidoId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(partidoId,
                    Partido.class.getName());
            }

            /* Get results. */
            i = 1;
            String rival = resultSet.getString(i++);
            int entradasVendidas = resultSet.getInt(i++);
            int entradasIniciales = resultSet.getInt(i++);
            float price = resultSet.getFloat(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
            Timestamp datepAsTimeStamp = resultSet.getTimestamp(i++);
            LocalDateTime datep = datepAsTimeStamp.toLocalDateTime();

            /* Return movie. */
            return new Partido(partidoId, price, datep, rival, entradasVendidas, entradasIniciales, creationDate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Partido> findByDate(Connection connection, LocalDateTime date1, LocalDateTime date2) {
        String queryString = "SELECT partidoId, rival, entradasVendidas, entradasIniciales, price, creationDate," +
                "datep FROM Partido WHERE datep >= ? AND datep <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int j = 1;
            preparedStatement.setObject(j++, date1);
            preparedStatement.setObject(j++, date2);


            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read movies. */
            List<Partido> partidos = new ArrayList<Partido>();

            while (resultSet.next()) {

                int i = 1;
                Long partidoId = Long.valueOf(resultSet.getLong(i++));
                String rival = resultSet.getString(i++);
                int entradasVendidas = resultSet.getInt(i++);
                int entradasIniciales = resultSet.getInt(i++);
                float price = resultSet.getFloat(i++);
                Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime creationDate = creationDateAsTimestamp.toLocalDateTime();
                Timestamp datepAsTimeStamp = resultSet.getTimestamp(i++);
                LocalDateTime datep = datepAsTimeStamp.toLocalDateTime();
                partidos.add(new Partido(partidoId, price, datep, rival, entradasVendidas, entradasIniciales, creationDate));

            }

            /* Return movies. */
            return partidos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Partido partido) throws InstanceNotFoundException {
        String queryString = "UPDATE Partido SET entradasVendidas  = ? WHERE partidoId = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setInt(i++, partido.getEntradasVendidas());
            preparedStatement.setLong(i++, partido.getPartidoId());

            int updatedRows = preparedStatement.executeUpdate();

            if(updatedRows == 0) {
                throw new InstanceNotFoundException(partido.getPartidoId(), Partido.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long partidoId) throws InstanceNotFoundException {

    }
}
