package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;


public class JsonToClientExceptionConversor {
  private static InputValidationException toInputValidationException(JsonNode rootNode) {
    String message = rootNode.get("message").textValue();
    return new InputValidationException(message);
  }

  public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(ex);
      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object unexpected)");
      } else {
        String errorType = rootNode.get("errorType").textValue();
        if (errorType.equals("InputValidation")) {
          return toInputValidationException(rootNode);
        } else {
          throw new ParsingException("Unrecognized error type: " + errorType);
        }
      }
    } catch (ParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
    String instanceId = rootNode.get("instanceId").textValue();
    String instanceType = rootNode.get("instanceType").textValue();
    return new InstanceNotFoundException(instanceId, instanceType);
  }

  public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
    try {
      ObjectMapper objectNode = ObjectMapperFactory.instance();
      JsonNode rootNode = objectNode.readTree(ex);
      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object unexpected)");
      } else {
        String errorType = rootNode.get("errorType").textValue();
        if (errorType.equals("InstanceNotFound")) {
          return toInstanceNotFoundException(rootNode);
        } else {
          throw new ParsingException("Unrecognized error type: " + errorType);
        }
      }
    } catch (ParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(ex);
      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object expected)");
      } else {
        String errorType = rootNode.get("errorType").textValue();
        return switch (errorType) {
          case "PartidoNotRemovableException" -> toPartidoNotRemovableException(rootNode);
          case "EntradasInsuficientes" -> toEntradasInsuficientesException(rootNode);
          case "AlreadyCollected" -> toAlreadyCollectedException(rootNode);
          case "InvalidCard" -> toInvalidCardException(rootNode);
          default -> throw new ParsingException("Unrecognized error type: " + errorType);
        };
      }
    } catch (ParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }

  public static ClientInvalidCardException toInvalidCardException(JsonNode rootNode) {
    Long saleId = rootNode.get("saleId").longValue();
    String creditCard = rootNode.get("creditCardNumber").textValue();
    return new ClientInvalidCardException(saleId, creditCard);
  }

  private static ClientPartidoNotRemovableException toPartidoNotRemovableException(JsonNode rootNode) {
    Long partidoId = rootNode.get("partidoId").longValue();
    return new ClientPartidoNotRemovableException(partidoId);
  }

  public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
    try {
      ObjectMapper objectMapper = ObjectMapperFactory.instance();
      JsonNode rootNode = objectMapper.readTree(ex);
      if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
        throw new ParsingException("Unrecognized JSON (object expected)");
      } else {
        String errorType = rootNode.get("errorType").textValue();
        if (errorType.equals("PartidoCelebrado")) {
          return toPartidoCelebradoException(rootNode);
        } else {
          throw new ParsingException("Unrecognized error type: " + errorType);
        }
      }
    } catch (ParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new ParsingException(e);
    }
  }
  private static ClientPartidoCelebradoException toPartidoCelebradoException (JsonNode rootNode) {
    Long partidoId = rootNode.get("partidoId").longValue();
    String partidoDateAsString = rootNode.get("datep").textValue();
    LocalDateTime partidoDate = null;
    if (partidoDateAsString != null) {
      partidoDate = LocalDateTime.parse(partidoDateAsString);
    }
    return new ClientPartidoCelebradoException(partidoId, partidoDate);
  }

  private static ClientEntradasInsuficientesException toEntradasInsuficientesException (JsonNode rootNode) {
    Long saleId = rootNode.get("partidoId").longValue();
    int amount = rootNode.get("amount").intValue();
    return new ClientEntradasInsuficientesException(saleId, amount);
  }

  private static ClientAlreadyCollectedException toAlreadyCollectedException (JsonNode rootNode) {
    Long partidoId = rootNode.get("partidoId").longValue();
    String creditCardNumber = rootNode.get("creditCardNumber").textValue();
    return new ClientAlreadyCollectedException(partidoId, creditCardNumber);
  }
}
