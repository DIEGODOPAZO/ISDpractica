package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.sale.Sale;

import java.util.ArrayList;
import java.util.List;

public class SaleToRestSaleDtoConversor {

  public static RestSaleDto toRestSaleDto(Sale sale) {
    String creditCard = sale.getCreditCardNumber();
    return new RestSaleDto(sale.getSaleID(), sale.getPartidoId(), creditCard,
        sale.getUserMail(), sale.getAmount(), sale.getDelivered());
  }

  public static List<RestSaleDto> toRestSaleDtos(List<Sale> sales) {
    List<RestSaleDto> saleDtos = new ArrayList<>(sales.size());

    for (Sale sale : sales) {
      saleDtos.add(toRestSaleDto(sale));
    }
    return saleDtos;
  }

  public static Sale toSale(RestSaleDto saleDto) {
    return new Sale(saleDto.getSaleId(), saleDto.getPartidoId(),
                saleDto.getUserMail(), saleDto.getCreditCardNumber(), null, saleDto.getAmount(), saleDto.isDelivered());
  }

}
