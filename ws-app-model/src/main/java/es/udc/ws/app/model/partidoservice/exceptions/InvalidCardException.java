package es.udc.ws.app.model.partidoservice.exceptions;

public class InvalidCardException extends Exception{

    private Long saleId;
    private String cardNumber;

    public InvalidCardException(Long saleId, String cardNumber){
        super("La tarjeta \"" + cardNumber + "\n no se corresponde con la que se realizo la compra de la entrada \"" + saleId );
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
