package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToRestPartidoDtoConversor {

    public static ObjectNode toObjectNode(RestPartidoDto partido) {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        partidoObject.put("partidoId", partido.getPartidoId()).
                put("rival", partido.getRival()).
                put("date", partido.getFechaPartido().toString()).
                put("price", partido.getPrice()).
                put("entradasVendidas", partido.getEntradasVendidas()).
                put("entradasIniciales", partido.getEntradasIniciales());

        return partidoObject;
    }

    public static ArrayNode toArrayNode(List<RestPartidoDto> partidos) {

        ArrayNode partidosNode = JsonNodeFactory.instance.arrayNode();
        for (RestPartidoDto partidoDto : partidos) {
            ObjectNode partidoObject = toObjectNode(partidoDto);
            partidosNode.add(partidoObject);
        }

        return partidosNode;
    }

    public static RestPartidoDto toRestPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) rootNode;

                JsonNode partidoIdNode = partidoObject.get("partidoId");
                Long partidoId = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

                String rival = partidoObject.get("rival").textValue().trim();

                // Formato
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                // Convertir la cadena a LocalDateTime
                LocalDateTime date = LocalDateTime.parse(partidoObject.get("date").textValue(), formatter);

                int entradasIniciales =  partidoObject.get("entradasIniciales").intValue();
                int entradasVendidas =  partidoObject.get("entradasIniciales").intValue() - partidoObject.get("entradasDisponibles").intValue();
                float price = partidoObject.get("price").floatValue();

                return new RestPartidoDto(partidoId, price, entradasIniciales, entradasVendidas, date, rival);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
