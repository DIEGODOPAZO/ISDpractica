package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientSaleDtoConversor {

  public static ClientSaleDto toClientSaleDto(InputStream jsonSale) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(jsonSale);
      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object expected)");
      } else {
        ObjectNode partidoObject = (ObjectNode) rootNode;

        JsonNode saleIdNode = partidoObject.get("saleId");
        Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;

        Long partidoId = partidoObject.get("partidoId").longValue();
        String userMail = partidoObject.get("userMail").textValue().trim();
        String crediCardNumber = partidoObject.get("creditCardNumber").textValue().trim();
        int amount = partidoObject.get("amount").intValue();
        boolean delivered = partidoObject.get("delivered").asBoolean();

        return new ClientSaleDto(saleId, partidoId, userMail, crediCardNumber, amount, delivered);
      }
    } catch (ParsingException ex) {
      throw ex;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  public static List<ClientSaleDto> toClientSaleDtos(InputStream jsonSales) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(jsonSales);

      if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
        throw new ParsingException("Unrecognized JSON (array expected)");
      } else {
        ArrayNode salesArray = (ArrayNode) rootNode;
        List<ClientSaleDto> saleDtos = new ArrayList<>(salesArray.size());

        for (JsonNode saleNode : salesArray) {
          saleDtos.add(toClientSaleDto((ObjectNode) saleNode));
        }

        return saleDtos;
      }
    } catch (ParsingException ex) {
      throw ex;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  public static ClientSaleDto toClientSaleDto(ObjectNode saleNode) throws ParsingException {
    try {
      Long saleId = (saleNode.has("saleId")) ? saleNode.get("saleId").longValue() : null;
      Long partidoId = saleNode.get("partidoId").longValue();
      String userMail = saleNode.get("userMail").textValue().trim();
      String creditCardNumber = saleNode.get("creditCardNumber").textValue().trim();
      int amount = saleNode.get("amount").intValue();
      boolean delivered = saleNode.get("delivered").asBoolean();

      return new ClientSaleDto(saleId, partidoId, userMail, creditCardNumber, amount, delivered);
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }
}
