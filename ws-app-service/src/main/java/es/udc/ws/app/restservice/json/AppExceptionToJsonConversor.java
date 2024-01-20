package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.partidoservice.exceptions.*;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class AppExceptionToJsonConversor {
  public static ObjectNode toPartidoNotRemovableExpception(PartidoNotRemovableException ex) {
    ObjectNode exceptionObjetct = JsonNodeFactory.instance.objectNode();

    exceptionObjetct.put("errorType", "PartidoNotRemovable");
    exceptionObjetct.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);

    return exceptionObjetct;
  }

  public static ObjectNode toPartidoCelebradoException(PartidoCelebradoException ex) {
    ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

    exceptionObject.put("errorType", "PartidoCelebrado");
    exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
    if (ex.getPartidoDate() != null) {
      exceptionObject.put("datep", ex.getPartidoDate().toString());
    } else {
      exceptionObject.set("datep", null);
    }

    return exceptionObject;
  }

  public static ObjectNode toInvalidCardException(InvalidCardException ex) {
    ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

    exceptionObject.put("errorType", "InvalidCard");
    exceptionObject.put("saleId", (ex.getSaleId() != null) ? ex.getSaleId() : null);
    if (ex.getCardNumber() != null) {
      exceptionObject.put("creditCardNumber", ex.getCardNumber());
    } else {
      exceptionObject.set("creditCardNumber", null);
    }

    return exceptionObject;
  }

  public static ObjectNode toEntradasInsuficientesException(EntradasInsuficientesException ex) {
    ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

    exceptionObject.put("errorType" ,"EntradasInsuficientes");
    exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
    exceptionObject.put("amount", ex.getAmount());

    return exceptionObject;
  }

  public static ObjectNode toAlreadyCollectedException(AlreadyCollectedException ex) {
    ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

    exceptionObject.put("errorType", "AlreadyCollected");
    exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
    if (ex.getCardNumber() != null) {
      exceptionObject.put("creditCardNumber", ex.getCardNumber());
    } else {
      exceptionObject.set("creditCardNumber", null);
    }

    return exceptionObject;
  }

  public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex) {
    ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    exceptionObject.put("errorType", "InstanceNotFound");
    exceptionObject.put("instanceId", ex.getInstanceId().toString());
    exceptionObject.put("instanceType", ex.getInstanceType());
    return exceptionObject;
  }
}
