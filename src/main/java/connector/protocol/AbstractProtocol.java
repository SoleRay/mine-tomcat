package connector.protocol;

import connector.endpoint.AbstractEndpoint;

public class AbstractProtocol<U> implements ProtocolHandler {

    private final AbstractEndpoint<U> endpoint;

    public AbstractProtocol(AbstractEndpoint<U> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void init() throws Exception {
        endpoint.init();
    }

    @Override
    public void start() throws Exception {
        endpoint.start();
    }

    @Override
    public void pause() throws Exception {

    }

    @Override
    public void resume() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    public AbstractEndpoint<U> getEndpoint() {
        return endpoint;
    }
}
