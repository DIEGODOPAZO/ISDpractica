package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientPartidoDtoToThriftPartidoDtoConversor {

    public static ThriftPartidoDto toThriftPartidoDto(
            ClientPartidoDto clientPartidoDto) {

        Long partidoId = clientPartidoDto.getPartidoId();
        int entradasVendidas = clientPartidoDto.getEntradasIniciales() - clientPartidoDto.getEntradasDisponibles();
        return new ThriftPartidoDto(partidoId == null ? -1 : partidoId, clientPartidoDto.getRival(), entradasVendidas,
                clientPartidoDto.getEntradasIniciales(), clientPartidoDto.getPrice(),
                clientPartidoDto.getFechaPartido().toString());

    }

    public static List<ClientPartidoDto> toClientPartidoDtos(List<ThriftPartidoDto> partidos) {

        List<ClientPartidoDto> clientPartidoDtos = new ArrayList<>(partidos.size());

        for (ThriftPartidoDto partido : partidos) {
            clientPartidoDtos.add(toClientPartidoDto(partido));
        }
        return clientPartidoDtos;

    }

    public static ClientPartidoDto toClientPartidoDto(ThriftPartidoDto partido) {
        int entradasDisponibles = partido.getEntradasIniciales() - partido.getEntradasVendidas();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime fecha = LocalDateTime.parse(partido.getFechaPartido(), formatter);

        return new ClientPartidoDto(partido.partidoId, (float) partido.getPrice(),
                entradasDisponibles,
                partido.getEntradasIniciales(), fecha, partido.getRival());

    }

}
