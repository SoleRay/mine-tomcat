package connector.protocol;

import connector.endpoint.AbstractEndpoint;

public abstract class AbstractHttp11Protocol extends AbstractProtocol {

    public AbstractHttp11Protocol(AbstractEndpoint endpoint) {
        super(endpoint);
    }
}
