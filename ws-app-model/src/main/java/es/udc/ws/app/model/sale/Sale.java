package es.udc.ws.app.model.sale;

import java.time.LocalDateTime;

public class Sale {
  private Long saleID;
  private Long partidoId;
  private String userMail;
  private LocalDateTime saleDate;
  private String creditCardNumber;
  private int amount;
  private boolean delivered;

  public Sale(Long matchID, String userMail, String creditCardNumber, LocalDateTime saleDate,
              int amount, boolean delivered) {
    this.partidoId = matchID;
    this.userMail = userMail;
    this.creditCardNumber = creditCardNumber;
    this.saleDate = (saleDate != null) ? saleDate.withNano(0) : null;
    this.amount = amount;
    this.delivered = delivered;
  }

  public Sale(Long saleID, Long matchID, String userMail, String creditCardNumber, LocalDateTime saleDate,
              int amount, boolean delivered) {
    this.saleID = saleID;
    this.partidoId = matchID;
    this.userMail = userMail;
    this.saleDate = (saleDate != null) ? saleDate.withNano(0) : null;
    this.creditCardNumber = creditCardNumber;
    this.amount = amount;
    this.delivered = delivered;
  }



  public Long getSaleID() {
    return saleID;
  }

  public void setSaleID(Long saleID) {
    this.saleID = saleID;
  }

  public Long getPartidoId() {
    return partidoId;
  }

  public void setPartidoId(Long matchID) {
    this.partidoId = matchID;
  }

  public String getUserMail() {
    return userMail;
  }

  public void setUserMail(String userMail) {
    this.userMail = userMail;
  }

  public LocalDateTime getSaleDate() {
    return saleDate;
  }

  public void setSaleDate(LocalDateTime saleDate) {
    this.saleDate = saleDate;
  }

  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  public void setCreditCardNumber(String creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public boolean getDelivered() {
    return delivered;
  }

  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
    result = prime * result + amount;
    result = prime * result + ((partidoId == null) ? 0 : partidoId.hashCode());
    result = prime * result + ((saleID == null) ? 0 : saleID.hashCode());
    result = prime * result + ((saleDate == null) ? 0 : saleDate.hashCode());
    result = prime * result + ((userMail == null) ? 0 : userMail.hashCode());
    result = prime * result + (delivered ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Sale other = (Sale) obj;

    //Comprobaciones de cada una de las variables
    if(delivered != other.delivered){
      return false;
    }
    if (creditCardNumber == null) {
      if (other.creditCardNumber != null)
        return false;
    } else if (!creditCardNumber.equals(other.creditCardNumber))
      return false;
    if (saleID == null) {
      if (other.saleID != null)
        return false;
    } else if (!saleID.equals(other.saleID))
      return false;
    if (partidoId == null) {
      if (other.partidoId != null)
        return false;
    } else if (!partidoId.equals(other.partidoId))
      return false;
    if (userMail == null) {
      if (other.userMail != null)
        return false;
    } else if (!userMail.equals(other.userMail))
      return false;
    if (saleDate == null) {
      if (other.saleDate != null)
        return false;
    } else if (!saleDate.equals(other.saleDate))
      return false;
    if (amount != other.amount)
      return false;
    if (saleDate == null) {
      if (other.saleDate != null) return false;
    } else if (!saleDate.equals(other.saleDate))
      return false;
    return true;
  }
}
