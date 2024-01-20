package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.sale.Sale;
import es.udc.ws.app.thrift.ThriftSaleDto;

public class SaleToThriftSaleDtoConversor {
  public static ThriftSaleDto toThriftSaleDto(Sale sale) {

    return new ThriftSaleDto(sale.getSaleID(), sale.getPartidoId(), sale.getCreditCardNumber(),
        sale.getUserMail(), sale.getAmount());
  }
}
