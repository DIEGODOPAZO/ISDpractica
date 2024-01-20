package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PartidoToThriftPartidoDtoConversor {

    public static Partido toPartido(ThriftPartidoDto partido) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime fechaPartido = LocalDateTime.parse(partido.getFechaPartido(), formatter);
        return new Partido(partido.getPartidoId(), (float) partido.getPrice(),
                fechaPartido, partido.getRival(), partido.getEntradasVendidas(), partido.getEntradasIniciales());
    }

    public static List<ThriftPartidoDto> toThriftPartidoDtos(List<Partido> partidos) {

        List<ThriftPartidoDto> dtos = new ArrayList<>(partidos.size());

        for (Partido partido : partidos) {
            dtos.add(toThriftPartidoDto(partido));
        }
        return dtos;

    }

    public static ThriftPartidoDto toThriftPartidoDto(Partido partido) {

        return new ThriftPartidoDto(partido.getPartidoId(), partido.getRival(),
                partido.getEntradasVendidas(), partido.getEntradasIniciales(), partido.getPrice(), partido.getDate().toString());
    }

}

