package es.udc.ws.app.restservice.dto;
import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.partido.Partido;

public class PartidoToRestPartidoDtoConversor {

    public static List<RestPartidoDto> toRestPartidoDtos(List<Partido> partidos) {
        List<RestPartidoDto> partidoDtos = new ArrayList<>(partidos.size());

        for (Partido partido : partidos) {
            partidoDtos.add(toRestPartidoDto(partido));
        }
        return partidoDtos;
    }

    public static RestPartidoDto toRestPartidoDto(Partido partido) {
        return new RestPartidoDto(partido.getPartidoId(), partido.getPrice(), partido.getEntradasIniciales(), partido.getEntradasVendidas(), partido.getDate(), partido.getRival());
    }

    public static Partido toPartido(RestPartidoDto partido) {
        return new Partido(partido.getPartidoId(), partido.getPrice(),
                partido.getFechaPartido(), partido.getRival(), partido.getEntradasVendidas(), partido.getEntradasIniciales());
    }

}
