package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientPartidoDto {

    private Long partidoId;

    private int entradasDisponibles;
    private int entradasIniciales;
    private LocalDateTime fechaPartido;
    private String rival;
    private float price;

    public ClientPartidoDto() {
    }

    public ClientPartidoDto(Long partidoId, float price, int entradasDisponibles, int entradasIniciales,LocalDateTime fechaPartido, String rival){
        this.partidoId = partidoId;
        this.entradasDisponibles = entradasDisponibles;
        this.entradasIniciales = entradasIniciales;
        this.fechaPartido = fechaPartido;
        this.rival = rival;
        this.price = price;
    }



    // geters and setters

    public int getEntradasIniciales() {
        return entradasIniciales;
    }

    public void setEntradasIniciales(int entradasIniciales) {
        this.entradasIniciales = entradasIniciales;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    public void setEntradasDisponibles(int entradasDisponibles) {
        this.entradasDisponibles = entradasDisponibles;
    }

    public LocalDateTime getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(LocalDateTime fechaPartido) {
        this.fechaPartido = fechaPartido;
    }


    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PartidoDto [partidoId=" + partidoId + ", rival=" + rival
                + ", entradas disponibles=" + entradasDisponibles
                + ", fecha=" + fechaPartido + ", price=" + price + "]";
    }

}
