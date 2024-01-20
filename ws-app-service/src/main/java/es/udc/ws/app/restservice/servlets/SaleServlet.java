package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.model.partidoservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.partidoservice.exceptions.EntradasInsuficientesException;
import es.udc.ws.app.model.partidoservice.exceptions.InvalidCardException;
import es.udc.ws.app.model.partidoservice.exceptions.PartidoCelebradoException;
import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.app.restservice.dto.RestSaleDto;
import es.udc.ws.app.restservice.dto.SaleToRestSaleDtoConversor;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestSaleDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleServlet extends RestHttpServletTemplate {
  @Override
  protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
          InputValidationException, InstanceNotFoundException {

    if (req.getPathInfo() == "/" || req.getPathInfo() == null) {

      ServletUtils.checkEmptyPath(req);
      Long partidoId = ServletUtils.getMandatoryParameterAsLong(req, "partidoId");
      String userMail = ServletUtils.getMandatoryParameter(req, "userMail");
      String credirCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");
      int amount = ServletUtils.getMandatoryParameterAsLong(req, "amount").intValue();

      try {
        Sale sale = PartidoServiceFactory.getService().buyEntradas(partidoId, userMail, credirCardNumber, amount);

        RestSaleDto saleDto = SaleToRestSaleDtoConversor.toRestSaleDto(sale);
        String saleURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + sale.getSaleID().toString();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", saleURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestSaleDtoConversor.toObjectNode(saleDto), headers);
      } catch (PartidoCelebradoException e) {
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                AppExceptionToJsonConversor.toPartidoCelebradoException(e), null);
      } catch (EntradasInsuficientesException e) {
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                AppExceptionToJsonConversor.toEntradasInsuficientesException(e), null);
      }

    } else {

      Long saleId = ServletUtils.getIdFromPath(req, "saleId");
      String creditCard = ServletUtils.getMandatoryParameter(req, "creditCardNumber");

     try{
       PartidoServiceFactory.getService().deliverTicket(saleId, creditCard);
     } catch (InvalidCardException e) {
       ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toInvalidCardException(e), null);
     } catch (AlreadyCollectedException e) {
       ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toAlreadyCollectedException(e), null);
     }

      ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

  }


  @Override
  protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
          InputValidationException {

    ServletUtils.checkEmptyPath(req);
    String userMail = req.getParameter("userMail");

    List<Sale> sales = PartidoServiceFactory.getService().findUserSales(userMail);

    List<RestSaleDto> saleDtos = SaleToRestSaleDtoConversor.toRestSaleDtos(sales);
    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
            JsonToRestSaleDtoConversor.toArrayNode(saleDtos), null);
  }

}
