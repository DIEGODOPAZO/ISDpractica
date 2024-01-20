namespace java es.udc.ws.app.thrift

struct ThriftPartidoDto {
    1: i64 partidoId
    2: string rival
    3: i32 entradasVendidas
    4: i32 entradasIniciales
    5: double price
    6: string fechaPartido
}

struct ThriftSaleDto {
    1: i64 saleId
    2: i64 partidoId
    3: string creditCardNumber
    4: string userMail
    5: i32 amount
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftEntradasInsuficientesException {
    1: i64 partidoId
    2: i32 amount
}

exception ThriftPartidoCelebradoException {
    1: i64 partidoId
    2: string partidoDate
}

service ThriftPartidoService {

   ThriftPartidoDto addPartido(1: ThriftPartidoDto partidoDto) throws (1: ThriftInputValidationException e)

   list<ThriftPartidoDto> findPartidos(1: string date)

   ThriftSaleDto buyEntradas(1: i64 partidoId, 2: string creditCardNumber, 3: string userMail, 4: i32 amount)
   throws (1: ThriftInputValidationException e, 2: ThriftEntradasInsuficientesException ee,
   3: ThriftInstanceNotFoundException eee, 4: ThriftPartidoCelebradoException eeee)

   ThriftPartidoDto findById(1: i64 partidoId) throws (1: ThriftInstanceNotFoundException e)

}