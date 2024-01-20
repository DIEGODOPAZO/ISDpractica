package es.udc.ws.app.model.partido;


import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {

    private long partidoId;
    private float price;
    private LocalDateTime date; //fecha del partido
    private LocalDateTime creationDate; //fecha de creacion del partido
    private String rival;
    private int entradasVendidas = -1;

    private int entradasIniciales = -1;
    private int entradasDisponibles = -1;

    public Partido(Long partidoId, float price, LocalDateTime date, String rival, int disponibles){
        this.partidoId = partidoId;
        this.price = price;
        this.date = date;
        this.creationDate = LocalDateTime.now().withNano(0) ;
        this.rival = rival;
        this.entradasDisponibles = disponibles;
    }

    public Partido(float price, LocalDateTime date, String rival, int entradasVendidas, int entradasIniciales){
        this.price = price;
        this.date = date;
        this.creationDate = LocalDateTime.now().withNano(0) ;
        this.rival = rival;
        this.entradasVendidas = entradasVendidas;
        this.entradasIniciales = entradasIniciales;
    }
    public Partido(long partidoId, float price, LocalDateTime date, String rival, int entradasVendidas, int entradasIniciales){
        this.partidoId = partidoId;
        this.price = price;
        this.date = date;
        this.creationDate = LocalDateTime.now().withNano(0) ;
        this.rival = rival;
        this.entradasVendidas = entradasVendidas;
        this.entradasIniciales = entradasIniciales;
    }

    public Partido(long partidoId, float price, LocalDateTime date, String rival, int entradasVendidas, int entradasIniciales, LocalDateTime creationDate){
        this.partidoId = partidoId;
        this.price = price;
        this.date = date;
        this.creationDate = creationDate;
        this.rival = rival;
        this.entradasVendidas = entradasVendidas;
        this.entradasIniciales = entradasIniciales;
    }

    public int getEntradasDisponibles(){
        if(this.entradasIniciales == -1 && this.entradasVendidas == -1){
            return this.entradasDisponibles;
        }else{
            this.entradasDisponibles = this.entradasIniciales - this.entradasVendidas;
            return this.entradasDisponibles;
        }
    }
    //getters
    public long getPartidoId() {
        return partidoId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public float getPrice() {
        return price;
    }

    public String getRival() {
        return rival;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public int getEntradasIniciales() {
        return entradasIniciales;
    }

    public void addEntradasVendidas(int amount) {this.entradasVendidas = entradasVendidas + amount;}
    //setters
    public void setPartidoId(long partidoId) {
        this.partidoId = partidoId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEntradasIniciales(int entradasIniciales) {
        this.entradasIniciales = entradasIniciales;
    }
    //Equals and hashCode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return partidoId == partido.partidoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(partidoId);
    }
}
