package es.udc.ws.app.model.partidoservice;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partidoservice.exceptions.*;

import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface PartidoService {

    public Partido addPartido(Partido partido) throws InputValidationException;

    public Partido  findPartidoById(Long Partido) throws InstanceNotFoundException;

    public List<Partido> findPartidoByDates(LocalDateTime date1, LocalDateTime date2);

    public Sale buyEntradas(Long partidoId, String userMail, String creditCard, int amount)
        throws InstanceNotFoundException, InputValidationException, PartidoCelebradoException, EntradasInsuficientesException;

    public void deliverTicket(Long saleId, String creditCardNumber) throws InstanceNotFoundException, InputValidationException, AlreadyCollectedException, InvalidCardException;

    public List<Sale> findUserSales(String userMail) throws InputValidationException;
}
