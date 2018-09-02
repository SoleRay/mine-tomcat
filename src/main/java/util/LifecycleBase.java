package util;

public abstract class LifecycleBase implements Lifecycle {

    @Override
    public final synchronized void init() {
        initInternal();
    }

    @Override
    public final synchronized void start() {
        startInternal();
    }

    @Override
    public final synchronized void stop() {
        stopInternal();
    }

    @Override
    public final synchronized void destroy() {
        destroyInternal();
    }

    protected abstract void initInternal();

    protected abstract void startInternal();

    protected abstract void stopInternal();

    protected abstract void destroyInternal();
}
