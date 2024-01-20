package es.udc.ws.app.model.util;



import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partido.SQLPartidoDao;
import es.udc.ws.app.model.partidoservice.exceptions.EntradasInsuficientesException;
import es.udc.ws.app.model.partidoservice.exceptions.PartidoCelebradoException;
import es.udc.ws.util.exceptions.InputValidationException;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class PropertyValidator {
    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    public static void validateDate(LocalDateTime datePartido) throws InputValidationException {
        if(datePartido.isBefore(LocalDateTime.now().withNano(0))){
            throw new InputValidationException("La fecha en la que se realiza el partido no puede ser antes que la fecha actual");
        }
    }

    public static void validateDateCreation(LocalDateTime datePartido, LocalDateTime dateCreation) throws InputValidationException {
        if(datePartido.isBefore(dateCreation)){
            throw new InputValidationException("La fecha en la que se realiza el partido no puede ser antes que la fecha de en la que se da de alta en el sistema");
        }
    }

    public static void validatePrice(Float price) throws InputValidationException {
        if(price < 0){
            throw new InputValidationException("El precio de una entrada no puede ser menor a 0");
        }
    }

    public static void validateRival(String rival) throws InputValidationException {
        if(rival == null){
            throw new InputValidationException("En cada partido hay siempre un rival");
        }else if(rival.trim().isEmpty()){
            throw new InputValidationException("En cada partido hay siempre un rival");
        }

    }
    public static void validateEntradasIniciales(int entradas) throws  InputValidationException{
        if(entradas <= 0){
            throw new InputValidationException("El numero de entradas iniciales tiene que ser mayor a cero");
        }
    }
    public static void validateEntradasVendidas(int entradas, int entradasTotales) throws InputValidationException {
        if(entradas > entradasTotales)
            throw new InputValidationException("El numero de entradas vendidas no puede ser mayor a las disponibles inicialmente");
        else if (entradas < 0)
            throw new InputValidationException("El numero de entradas vendidas no puede ser menor que 0");
    }

    public static void validateCreditCard (String creditCardNumber) throws InputValidationException {
        boolean validCreditCard = true;
        if ((creditCardNumber != null) && (creditCardNumber.length()==16)) {
            try{
                new BigInteger(creditCardNumber);
            } catch (NumberFormatException e) {
                validCreditCard = false;
            }
        } else {
            validCreditCard = false;
        }
        if (!validCreditCard) {
            throw new InputValidationException("Número de tarjeta no válido (tienen que ser 16 dígitos): " + creditCardNumber);
        }
    }

    public static void validateAmount (int amount, Partido partido) throws EntradasInsuficientesException {
        if (partido.getEntradasIniciales() - partido.getEntradasVendidas() < amount) {
            throw new EntradasInsuficientesException(partido.getPartidoId(), amount);
        }
    }

    public static void validateMayor0(int amount) throws InputValidationException {
        if (amount <= 0)
            throw new InputValidationException("No puede comprar una cantidad igual o inferior a 0");
    }

    public static void validateSaleDate (LocalDateTime saleDate, Partido partido) throws PartidoCelebradoException {
        if (saleDate.isAfter(partido.getDate()))
            throw new PartidoCelebradoException(partido.getPartidoId(), partido.getDate());
    }
    public static void validateUserMail(String userMail) throws InputValidationException{
        Matcher matcher = pattern.matcher(userMail);
        if (!matcher.matches())
            throw new InputValidationException(userMail + " no es un correo válido");
    }
}
