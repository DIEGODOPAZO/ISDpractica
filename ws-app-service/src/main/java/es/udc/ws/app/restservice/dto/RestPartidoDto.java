package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestPartidoDto {

    private Long partidoId;
    private int entradasIniciales;
    private int entradasVendidas;
    private LocalDateTime fechaPartido;
    private String rival;
    private float price;

    public RestPartidoDto() {
    }

    public RestPartidoDto(Long partidoId, float price, int entradasIniciales, int entradasVendias,LocalDateTime fechaPartido, String rival){
        this.partidoId = partidoId;
        this.entradasIniciales = entradasIniciales;
        this.entradasVendidas = entradasVendias;
        this.fechaPartido = fechaPartido;
        this.rival = rival;
        this.price = price;
    }


    // geters and setters
    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    public int getEntradasIniciales() {
        return entradasIniciales;
    }

    public void setEntradasIniciales(int entradasIniciales) {
        this.entradasIniciales = entradasIniciales;
    }

    public int getEntradasDisponibles() {
        return this.entradasIniciales - this.entradasVendidas;
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
                + ", entradas disponibles=" + this.getEntradasDisponibles()
                + ", fecha=" + fechaPartido + ", price=" + price + "]";
    }

}
