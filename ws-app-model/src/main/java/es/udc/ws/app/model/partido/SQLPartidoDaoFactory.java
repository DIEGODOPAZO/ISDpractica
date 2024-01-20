package es.udc.ws.app.model.partido;

//import es.udc.ws.app.util.configuration.ConfigurationParametersManager;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SQLPartidoDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SQLPartidoDaoFactory.className";
    private static SQLPartidoDao dao = null;

    private SQLPartidoDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SQLPartidoDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SQLPartidoDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SQLPartidoDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
