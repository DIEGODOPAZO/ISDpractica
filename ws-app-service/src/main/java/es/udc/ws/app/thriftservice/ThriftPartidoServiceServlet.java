package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftPartidoService;
import es.udc.ws.util.servlet.ThriftHttpServletTemplate;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

public class ThriftPartidoServiceServlet extends ThriftHttpServletTemplate {

    public ThriftPartidoServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {

        return new ThriftPartidoService.Processor<ThriftPartidoService.Iface>(
                new ThriftPartidoServiceImpl());

    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

}
