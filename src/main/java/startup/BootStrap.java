package startup;

import connector.Connector;
import connector.protocol.Http11NioProtocol;

public class BootStrap {

    public static void main(String[] args) {
        Http11NioProtocol protocol = new Http11NioProtocol();
        protocol.getEndpoint().setPort(8080);
        Connector connector = new Connector(protocol);
        connector.init();
        connector.start();
    }
}
