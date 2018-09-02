package util;

public abstract class LifecycleMBeanBase extends LifecycleBase{


    @Override
    protected void initInternal() {
        //TODO JMX Register
    }


    @Override
    protected void destroyInternal() {
        //TODO JMX UnRegister
    }
}
