package com.caodaxing.dubbo.factory;

import com.caodaxing.dubbo.annotation.DubboWrite;
import com.caodaxing.dubbo.annotation.Producer;
import com.caodaxing.dubbo.customer.InterfaceClassDynamicProxy;
import com.caodaxing.dubbo.producer.DubboProducerClient;
import com.caodaxing.dubbo.producer.DubboProducerServer;
import com.caodaxing.handler.ProducerAcceptCallMethodHandler;
import com.caodaxing.handler.ProducerRegisterMethodHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author daxing.cao
 * @version 0.0.1
 * @description 生产者注册工厂类
 */
@Slf4j
public class ProducerRegisterFactory extends ProducerConfiguration implements BeanFactoryAware, InstantiationAwareBeanPostProcessor {

    private BeanFactory beanFactory;
    private String host;
    private ExecutorService threadLoop = new ThreadPoolExecutor(10,20,5,
            TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        boolean isProducer = beanClass.getAnnotation(Producer.class) != null;
        if(isProducer){
            if(beanClass.isInterface()){
                throw new RuntimeException("Annotations 'Producer' cannot mark interface classes!");
            }
            if(beanClass.getInterfaces().length > 1){
                throw new RuntimeException("Classes that annotate 'Producer' tags can only implement one interface!");
            }
            if(log.isInfoEnabled()){
                log.info("start registering services with the registry...");
            }
            checkPropertyValues();
            this.registerServer(beanClass);
        }
        return null;
    }

    private void registerServer(Class beanClass) {
        if(beanClass == null){
            return;
        }
        if(StringUtils.isEmpty(host)){
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                this.host = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        Method[] methods = beanClass.getInterfaces()[0].getMethods();
        doRegisterServer(beanClass,methods);
    }

    private void doRegisterServer(Class beanClass,Method[] methods){
        for(Method method : methods){
            try {
                ProducerRegisterMethodHandler handler = new ProducerRegisterMethodHandler(beanClass,method,host,getProducerPort());
                new DubboProducerClient(handler).send(getRegisterHost(),getRegisterPort());
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field field : fields){
            DubboWrite annotation = field.getAnnotation(DubboWrite.class);
            if(annotation != null){
                try {
                    InterfaceClassDynamicProxy proxy = new InterfaceClassDynamicProxy(field.getType());
                    Object invoke = proxy.getProxy();
                    field.setAccessible(true);
                    field.set(bean,invoke);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void init(){
        BeanFactory factory = this.beanFactory;
        threadLoop.execute(() -> {
            ProducerAcceptCallMethodHandler handler = new ProducerAcceptCallMethodHandler(factory);
            new DubboProducerServer(handler).start(getProducerPort());
        });
    }

}
