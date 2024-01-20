package es.udc.ws.app.model.partidoservice.exceptions;

import java.time.LocalDateTime;

public class PartidoCelebradoException extends Exception {
  private Long partidoId;
  private LocalDateTime partidoDate;

  public PartidoCelebradoException(Long partidoId, LocalDateTime partidoDate) {
    super("Partido con id=\"" + partidoId + "\n ya se ha celebrado (tuvo lugar el " + partidoDate + ")");
    this.partidoId = partidoId;
    this.partidoDate = partidoDate;
  }

  public Long getPartidoId() {return partidoId;}
  public void setPartidoId(Long partidoId) {this.partidoId = partidoId;}
  public LocalDateTime getPartidoDate() {return partidoDate;}
  public void setPartidoDate(LocalDateTime partidoDate) {this.partidoDate = partidoDate;}
}
