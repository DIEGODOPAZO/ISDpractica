package es.udc.ws.app.model.sale;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.app.model.partidoservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.partidoservice.exceptions.InvalidCardException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlSaleDao {

  public Sale create(Connection connection, Sale sale);

  public Sale find(Connection connection, Long saleId)
      throws InstanceNotFoundException;

  public void update(Connection connection, Sale sale)
      throws InstanceNotFoundException;

  public void remove(Connection connection, Long saleId)
      throws InstanceNotFoundException;

  void updateDeliver(Connection connection, Sale sale, String creditCardNumber) throws InstanceNotFoundException, AlreadyCollectedException, InvalidCardException;

  List<Sale> findByUserMail(Connection connection, String userMail) throws InputValidationException;

  Sale findByMailCard(Connection connection, Long saleId, String creditCardNumber) throws InvalidCardException;

}
