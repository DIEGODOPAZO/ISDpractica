package es.udc.ws.app.client.service.exceptions;

public class ClientInvalidCardException extends Exception {
  private Long saleId;
  private String cardNumber;

  public ClientInvalidCardException(Long saleId, String cardNumber){
    super("La tarjeta " + cardNumber + " no se corresponde con la que se realizo la compra con id " + saleId );
    this.saleId = saleId;
    this.cardNumber = cardNumber;
  }

  public Long getSaleId() {
    return saleId;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setSaleId(Long saleId) {
    this.saleId = saleId;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }
}
