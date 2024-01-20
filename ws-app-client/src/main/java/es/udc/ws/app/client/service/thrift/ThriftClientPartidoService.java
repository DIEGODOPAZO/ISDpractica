package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientEntradasInsuficientesException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidCardException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoCelebradoException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftClientPartidoService implements ClientPartidoService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientPartidoService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
    @Override
    public Long addPartido(ClientPartidoDto partido) throws InputValidationException {
        ThriftPartidoService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.addPartido(ClientPartidoDtoToThriftPartidoDtoConversor.toThriftPartidoDto(partido)).getPartidoId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> findPartidoByDates(LocalDateTime date) {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDtos(client.findPartidos(date.toString()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto findPartidoById(Long partidoId) {

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDto(client.findById(partidoId));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long buyEntradas(Long partidoId, String userMail, String creditCardNumber, int amount)
        throws InputValidationException, InstanceNotFoundException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.buyEntradas(partidoId, userMail, creditCardNumber, amount).getSaleId();
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deliverTicket(Long saleId, String creditCardNumber) throws InstanceNotFoundException, InputValidationException, ClientInvalidCardException, ClientAlreadyCollectedException {

    }

    @Override
    public List<ClientSaleDto> findUserSales(String userMail) throws InputValidationException {
        return null;
    }

    private ThriftPartidoService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftPartidoService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
