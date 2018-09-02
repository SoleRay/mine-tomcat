package connector.protocol;

import connector.endpoint.NioEndpoint;

public class Http11NioProtocol extends AbstractHttp11JsseProtocol {

    public Http11NioProtocol() {
        super(new NioEndpoint());
    }


}
