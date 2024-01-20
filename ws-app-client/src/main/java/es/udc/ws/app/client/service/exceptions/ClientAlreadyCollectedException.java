package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyCollectedException extends Exception {

  private Long partidoId;
  private String cardNumber;

  public ClientAlreadyCollectedException(Long partidoId, String cardNumber){
    super("Las entradas para el partido " + partidoId + " correspondientes a la tarjeta " + cardNumber + "ya habian sido entregadas.");
    this.partidoId = partidoId;
    this.cardNumber = cardNumber;
  }

  public Long getPartidoId() {
    return partidoId;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setPartidoId(Long partidoId) {
    this.partidoId = partidoId;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }
}
