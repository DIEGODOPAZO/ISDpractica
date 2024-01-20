package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestSaleDto {
  private Long saleId;
  private Long partidoId;
  private String creditCardNumber;
  private String userMail;
  private int amount;
  private boolean delivered;

  public RestSaleDto () {}

  public RestSaleDto(Long saleId, Long partidoId, String creditCardNumber, String userMail, int amount, boolean delivered) {
    this.saleId = saleId;
    this.partidoId = partidoId;
    this.creditCardNumber = creditCardNumber;
    this.userMail = userMail;
    this.amount = amount;
    this.delivered = delivered;
  }

  public Long getSaleId() {return saleId;}
  public void setSaleId(Long saleId) {this.saleId = saleId;}

  public Long getPartidoId() {return partidoId;}
  public void setPartidoId(Long partidoId) {this.partidoId = partidoId;}

  public String getCreditCardNumber() {return creditCardNumber;}
  public void setCreditCardNumber(String creditCardNumber) {this.creditCardNumber = creditCardNumber;}

  public String getUserMail() {return userMail;}
  public void setUserMail(String userMail) {this.userMail = userMail;}

  public int getAmount() {return amount;}
  public void setAmount(int amount) {this.amount = amount;}

  public boolean isDelivered() {
    return delivered;
  }

  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }
}
