package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partidoservice.PartidoService;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.model.partidoservice.exceptions.EntradasInsuficientesException;
import es.udc.ws.app.model.partidoservice.exceptions.PartidoCelebradoException;
import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.thriftservice.PartidoToThriftPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftPartidoServiceImpl implements ThriftPartidoService.Iface{
    @Override
    public ThriftPartidoDto addPartido(ThriftPartidoDto partidoDto) throws ThriftInputValidationException, TException {
        Partido partido = PartidoToThriftPartidoDtoConversor.toPartido(partidoDto);

        try {
            Partido addedPartido = PartidoServiceFactory.getService().addPartido(partido);
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDto(addedPartido);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public List<ThriftPartidoDto> findPartidos(String date) throws TException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Convertir la cadena a LocalDateTime
        LocalDateTime dateL = LocalDateTime.parse(date, formatter);

        List<Partido> partidos = PartidoServiceFactory.getService().findPartidoByDates(LocalDateTime.now(), dateL);

        return PartidoToThriftPartidoDtoConversor.toThriftPartidoDtos(partidos);
    }

    @Override
    public ThriftSaleDto buyEntradas(long partidoId, String creditCardNumber, String userMail, int amount) throws
        ThriftInputValidationException, ThriftInstanceNotFoundException,
        ThriftEntradasInsuficientesException, ThriftPartidoCelebradoException {
        try{
            Sale sale = PartidoServiceFactory.getService().buyEntradas(partidoId, creditCardNumber, userMail, amount);
            return SaleToThriftSaleDtoConversor.toThriftSaleDto(sale);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (EntradasInsuficientesException e) {
            throw new ThriftEntradasInsuficientesException(e.getPartidoId(), e.getAmount());
        } catch (PartidoCelebradoException e) {
            throw new ThriftPartidoCelebradoException(e.getPartidoId(), e.getPartidoDate().toString());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftPartidoDto findById(long partidoId) throws ThriftInstanceNotFoundException{
        try {
            Partido partido = PartidoServiceFactory.getService().findPartidoById(partidoId);
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDto(partido);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        }
    }
}
