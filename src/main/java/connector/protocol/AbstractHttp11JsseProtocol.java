package connector.protocol;

import connector.endpoint.AbstractJsseEndpoint;

public class AbstractHttp11JsseProtocol extends AbstractHttp11Protocol {

    public AbstractHttp11JsseProtocol(AbstractJsseEndpoint endpoint) {
        super(endpoint);
    }
}
