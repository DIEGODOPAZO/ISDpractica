package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientEntradasInsuficientesException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidCardException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoCelebradoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientPartidoService {

    public Long addPartido(ClientPartidoDto partido)
            throws InputValidationException;

    public List<ClientPartidoDto> findPartidoByDates(LocalDateTime date);

    public ClientPartidoDto findPartidoById(Long partidoId);

    public Long buyEntradas(Long partidoId, String userMail, String creditCardNumber, int amount)
        throws InputValidationException, InstanceNotFoundException;

    public void deliverTicket(Long saleId, String creditCardNumber) throws InstanceNotFoundException, InputValidationException, ClientInvalidCardException, ClientAlreadyCollectedException;

    public List<ClientSaleDto> findUserSales(String userMail) throws InputValidationException;
}
