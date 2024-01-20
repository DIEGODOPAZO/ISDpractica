package es.udc.ws.app.client.service.exceptions;

public class ClientEntradasInsuficientesException extends Exception {
  private Long partidoId;
  private int amount;
  public ClientEntradasInsuficientesException(Long partidoId, int amount) {
    super("No se pueden comprar " + amount + " entradas para el partido " + partidoId);
    this.partidoId = partidoId;
    this.amount = amount;
  }
  public Long getPartidoId() {return partidoId;}
  public void setPartidoId(Long partidoId){this.partidoId = partidoId;}
  public int getAmount() {return amount;}
  public void setAmount(int amount){this.amount = amount;}
}
