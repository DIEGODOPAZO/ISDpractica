package es.udc.ws.app.model.partidoservice.exceptions;

public class PartidoNotRemovableException extends Exception{
  private Long partidoId;

  public PartidoNotRemovableException(Long partidoId) {
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
