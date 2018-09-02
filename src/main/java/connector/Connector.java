package connector;

import connector.protocol.ProtocolHandler;
import util.LifecycleMBeanBase;

public class Connector extends LifecycleMBeanBase {

    private ProtocolHandler protocol;

    public Connector(ProtocolHandler protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void initInternal() {
        try {
            protocol.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void startInternal() {
        try {
            protocol.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void stopInternal() {
        try {
            protocol.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
