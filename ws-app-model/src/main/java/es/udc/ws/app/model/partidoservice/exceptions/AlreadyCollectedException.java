package es.udc.ws.app.model.partidoservice.exceptions;

public class AlreadyCollectedException extends Exception{

    private Long partidoId;
    private String cardNumber;

    public AlreadyCollectedException(Long partidoId, String cardNumber){
        super("Las entradas para el partido \"" + partidoId + "\n correspondientes a la tarjeta \"" + cardNumber + "ya habian sido entregadas.");
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
