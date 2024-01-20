package es.udc.ws.app.restservice.json;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestSaleDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import java.io.InputStream;
import java.util.List;

public class JsonToRestSaleDtoConversor {
  public static ObjectNode toObjectNode(RestSaleDto sale) {
    ObjectNode saleNode = JsonNodeFactory.instance.objectNode();

    if (sale.getSaleId() != null) {
      saleNode.put("saleId", sale.getSaleId());
    }
    saleNode.put("partidoId", sale.getPartidoId()).
        put("creditCardNumber", sale.getCreditCardNumber().substring(sale.getCreditCardNumber().length()-4)).
        put("userMail", sale.getUserMail()).
        put("amount", sale.getAmount()).
        put("delivered", sale.isDelivered());
    return saleNode;
  }

  public static ArrayNode toArrayNode(List<RestSaleDto> sales) {

    ArrayNode salesNode = JsonNodeFactory.instance.arrayNode();

    if(!sales.isEmpty()){
      for (RestSaleDto saleDto : sales) {
        ObjectNode partidoObject = toObjectNode(saleDto);
        salesNode.add(partidoObject);
      }
    }

    return salesNode;
  }

  public static RestSaleDto toRestSaleDto(InputStream jsonSale) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(jsonSale);

      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object expected)");
      } else {
        ObjectNode saleObject = (ObjectNode) rootNode;

        JsonNode saleIdNode = saleObject.get("saleId");
        Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;

        JsonNode partidoIdNode = saleObject.get("partidoId");
        Long partidoId = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

        String creditCardNumber = saleObject.get("creditCardNumber").textValue().trim();
        String userMail = saleObject.get("userMail").textValue().trim();
        int amount = saleObject.get("amount").intValue();
        boolean delivered = saleObject.get("delivered").booleanValue();

        return new RestSaleDto(saleId, partidoId, creditCardNumber, userMail, amount, delivered);
      }
    } catch (ParsingException ex) {
      throw ex;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }


}
