package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyCollectedException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidCardException;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientPartidoDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientSaleDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;


public class RestClientPartidoService implements ClientPartidoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientPartidoService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addPartido(ClientPartidoDto partido) throws InputValidationException {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "partido").
                    bodyStream(toInputStream(partido), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent()).getPartidoId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> findPartidoByDates(LocalDateTime date) {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partido?date="
                            + URLEncoder.encode(date.toString(), StandardCharsets.UTF_8)).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ClientPartidoDto findPartidoById(Long partidoId) {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partido/"
                    + URLEncoder.encode(partidoId.toString(), StandardCharsets.UTF_8)).
                execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long buyEntradas(Long partidoId, String userMail, String creditCardNumber, int amount) throws
        InstanceNotFoundException, InputValidationException {
      try {
        ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "sales").
            bodyForm((
                Form.form().
                    add("partidoId", Long.toString(partidoId)).
                    add("userMail", userMail).
                    add("creditCardNumber", creditCardNumber).
                    add("amount", String.valueOf(amount)).build()
                )).execute().returnResponse();

        validateStatusCode(HttpStatus.SC_CREATED, response);

        return JsonToClientSaleDtoConversor.toClientSaleDto(response.getEntity().getContent()).getSaleId();
      } catch (InputValidationException | InstanceNotFoundException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public List<ClientSaleDto> findUserSales(String userMail) {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "sales?userMail="
                    + URLEncoder.encode(userMail, "UTF-8"))
                    .execute().returnResponse();
            
            validateStatusCode(HttpStatus.SC_OK, response);
            
            return JsonToClientSaleDtoConversor.toClientSaleDtos(response.getEntity().getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deliverTicket(Long saleId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException, ClientInvalidCardException, ClientAlreadyCollectedException {
        try {
            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "sales/" +
                    saleId).bodyForm().bodyForm(
                    (Form.form().add("creditCardNumber", creditCardNumber).build())).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException | ClientInvalidCardException | ClientAlreadyCollectedException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientPartidoDto partido) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientPartidoDtoConversor.toObjectNode(partido));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
