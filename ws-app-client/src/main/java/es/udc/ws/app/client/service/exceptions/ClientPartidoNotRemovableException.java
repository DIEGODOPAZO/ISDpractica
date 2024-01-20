package es.udc.ws.app.client.service.exceptions;

public class ClientPartidoNotRemovableException extends Exception {
  private Long partidoId;

  public ClientPartidoNotRemovableException(Long partidoId) {
    super("Partido with id=\"" + partidoId + "\n cannot be deleted because it has sales");
    this.partidoId = partidoId;
  }

  public Long getPartidoId() {
    return partidoId;
  }

  public void setPartidoId(Long partidoId) {
    this.partidoId = partidoId;
  }
}
