package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.ClientPartidoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientPartidoService clientPartidoService =
                ClientPartidoServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {1, 2});

            // [add] PartidoServiceClient -a <precio> <entradasDisp>  <fecha>  <rival>

            try {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                // Convertir la cadena a LocalDateTime
                LocalDateTime date = LocalDateTime.parse(args[3], formatter);

                Long partidoId = clientPartidoService.addPartido(new ClientPartidoDto(null,
                        Float.parseFloat(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[2]),date,
                        args[4]));

                System.out.println("Partido " + partidoId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fd".equalsIgnoreCase(args[0])){

            validateArgs(args, 2, new int[] {});

            // [find by dates] PartidoServiceClient -fd <date>

            try {
                // Formato
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                // Convertir la cadena a LocalDateTime
                LocalDateTime date = LocalDateTime.parse(args[1], formatter);

                List<ClientPartidoDto> partidos = clientPartidoService.findPartidoByDates(date);
                System.out.println("Found " + partidos.size() +
                        " partidos(s) before the date '" + args[1] + "'");
              for (ClientPartidoDto partidoDto : partidos) {
                System.out.println("Id: " + partidoDto.getPartidoId() +
                    ", Rival: " + partidoDto.getRival() +
                    ", Entradas Disponibles: " + (partidoDto.getEntradasDisponibles()) +
                     ", Entradas Totales: " + partidoDto.getEntradasIniciales() +
                    ", Fecha: " + partidoDto.getFechaPartido() +
                    ", Price: " + partidoDto.getPrice() + "\n");
              }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fi".equalsIgnoreCase(args[0])){

            validateArgs(args, 2, new int[] {1});

            // [find by id] PartidoServiceClient -fi <partidoId>

            try {
                ClientPartidoDto partido = clientPartidoService.findPartidoById(Long.parseLong(args[1]));
                    System.out.println("Datos del partido con Id " + partido.getPartidoId() + ":\n" +
                        "Rival: " + partido.getRival() +
                        ", Fecha de celebración: " + partido.getFechaPartido() +
                        ", Entradas disponibles: " + (partido.getEntradasDisponibles()) +
                        ", Entradas totales: " + (partido.getEntradasIniciales()) +
                        ", Price: " + partido.getPrice() + "\n");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int [] {1, 4});

            //[buy] PartidoServiceClient -b <partidoId> <userMail> <creditCardNumber> <amount>

            Long saleId;
            try {
                saleId = clientPartidoService.buyEntradas(Long.parseLong(args[1]), args[2], args[3],
                    Integer.parseInt(args[4]));

                System.out.println("Compradas " + args[4] + " entradas para el partido con Id "
                    + args[1] + " de forma existosa. Id de compra " + saleId);
            } catch (NumberFormatException | InstanceNotFoundException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-c".equalsIgnoreCase(args[0])){
            validateArgs(args, 3, new int[] {1});

            //[collect] PartidoServiceClient -c <purchaseId> <creditCardNumber>

            try {
                clientPartidoService.deliverTicket(Long.parseLong(args[1]), args[2]);
                System.out.println("Recogidas entradas con numero de compra " + args[1]);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if("-fp".equalsIgnoreCase(args[0])){
            validateArgs(args, 2, new int[] {});

            //[findPurchases] PartidoServiceClient -fp <userEmail>

            try {
                List<ClientSaleDto> sales = clientPartidoService.findUserSales(args[1]);
                String entregada = "No";
                System.out.println("Found " + sales.size() +
                    " sales for the user " + args[1]);
                for(ClientSaleDto saleDto: sales){
                    if(saleDto.isDelivered() == true){
                        entregada = "Si";
                    }else if(saleDto.isDelivered() == false){
                        entregada = "No";
                    }
                    System.out.println("Datos de la compra con Id: " + saleDto.getSaleId() + "\n" +
                            "matchId: " + saleDto.getPartidoId() +
                            ", Entradas: " + saleDto.getAmount() +
                            ", Tarjeta acabada en: " + saleDto.getCreditCardNumber() +
                            ", Recogidas: " + entregada +
                            ", Correo usuario: " + saleDto.getUserMail());
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }

        //Añadir aqui elseif para cada caso de uso

    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
      for (int position : numericArguments) {
        try {
          Double.parseDouble(args[position]);
        } catch (NumberFormatException n) {
          printUsageAndExit();
        }
      }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        //Añadir aqui como se usa para cada caso de uso
        System.err.println("""
            Usage:
                [add]    PartidoServiceClient -a <precio> <entradasDisp> <fecha>  <rival>
                [find by dates] PartidoServiceClient -fd <date>
                [find by id] PartidoServiceClient -fi <partidoId>
                [buy entradas] PartidoServiceClient -b <partidoId> <userMail> <creditCardNumber> <amount>
                [collect] PartidoServiceClient -c <purchaseId> <creditCardNumber>
                [find purchases] PartidoServiceClient -fp <userEmail>
            """);
    }

}
