package es.udc.ws.app.client.service.dto;

public class ClientSaleDto {
  private Long saleId;
  private Long partidoId;
  private String userMail;
  private String creditCardNumber;
  private int amount;
  private boolean delivered;

  public ClientSaleDto(){}
  public ClientSaleDto(Long saleId, Long partidoId, String userMail, String creditCardNumber, int amount, boolean delivered) {
    this.saleId = saleId;
    this.partidoId = partidoId;
    this.userMail = userMail;
    this.creditCardNumber = creditCardNumber;
    this.amount = amount;
    this.delivered = delivered;
  }

  public Long getSaleId() {return  saleId;}
  public void setSaleId(Long saleId) {this.saleId = saleId;}

  public Long getPartidoId() {return partidoId;}
  public void setPartidoId(Long partidoId) {this.partidoId = partidoId;}

  public String getUserMail() {return userMail;}
  public void setUserMail(String userMail) {this.userMail = userMail;}

  public String getCreditCardNumber() {return creditCardNumber;}
  public void setCreditCardNumber(String creditCardNumber) {this.creditCardNumber = creditCardNumber;}

  public int getAmount() {return amount;}
  public void setAmount(int amount) {this.amount = amount;}

  public boolean isDelivered() {
    return delivered;
  }
}
