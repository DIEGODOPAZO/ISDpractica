package es.udc.ws.app.model.partido;



import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SQLPartidoDao {

    public Partido create(Connection connection, Partido partido);

    public Partido findById(Connection connection, Long partidoId)
            throws InstanceNotFoundException;

    public List<Partido> findByDate(Connection connection, LocalDateTime date1, LocalDateTime date2);

    public void update(Connection connection, Partido partido)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long partidoId)
            throws InstanceNotFoundException;

}
