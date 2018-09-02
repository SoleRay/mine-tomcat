package connector.endpoint;

import util.net.SocketProperties;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEndpoint<U> {

    private int port;

    private InetAddress address;

    private int acceptCount = 100;

    protected int acceptorThreadCount = 1;

    protected volatile boolean running = false;

    protected volatile boolean paused = false;

    private boolean daemon = false;

    protected int acceptorThreadPriority = Thread.NORM_PRIORITY;

    private boolean bindOnInit = true;

    protected final SocketProperties socketProperties = new SocketProperties();

    private volatile BindState bindState = BindState.UNBOUND;


    protected List<Acceptor> acceptors;

    protected enum BindState {
        UNBOUND, BOUND_ON_INIT, BOUND_ON_START, SOCKET_CLOSED_ON_STOP
    }

    public final void init() throws Exception {
        if (bindOnInit) {
            bind();
            bindState = BindState.BOUND_ON_INIT;
        }
    }

    public final void start() throws Exception {
        if (bindState == BindState.UNBOUND) {
            bind();
            bindState = BindState.BOUND_ON_START;
        }
        startInternal();
    }

    public final void stop() throws Exception {
        stopInternal();
        if (bindState == BindState.BOUND_ON_START || bindState == BindState.SOCKET_CLOSED_ON_STOP) {
            unbind();
            bindState = BindState.UNBOUND;
        }
    }

    public final void destroy() throws Exception {
        if (bindState == BindState.BOUND_ON_INIT) {
            unbind();
            bindState = BindState.UNBOUND;
        }
    }


    protected final void startAcceptorThreads() {
        int count = getAcceptorThreadCount();
        acceptors = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Acceptor acceptor = new Acceptor(this);
            acceptors.add(acceptor);
            Thread t = new Thread(acceptor);
            t.setPriority(getAcceptorThreadPriority());
            t.setDaemon(getDaemon());
            t.start();
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        if (acceptCount > 0) {
            this.acceptCount = acceptCount;
        }
    }

    public void setAcceptorThreadCount(int acceptorThreadCount) {
        this.acceptorThreadCount = acceptorThreadCount;
    }

    public int getAcceptorThreadCount() {
        return acceptorThreadCount;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean getDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public int getAcceptorThreadPriority() {
        return acceptorThreadPriority;
    }

    public void setAcceptorThreadPriority(int acceptorThreadPriority) {
        this.acceptorThreadPriority = acceptorThreadPriority;
    }

    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;

    public abstract void startInternal() throws Exception;

    public abstract void stopInternal() throws Exception;

    protected abstract U serverSocketAccept() throws Exception;

    protected abstract boolean setSocketOptions(U socket);

    protected abstract void closeSocket(U socket);

    protected void destroySocket(U socket) {
        closeSocket(socket);
    }

    protected abstract void process() throws Exception;
}
