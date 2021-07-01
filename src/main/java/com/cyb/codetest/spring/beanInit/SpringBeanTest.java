package com.cyb.codetest.spring.beanInit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author cyb
 * @date 2021/1/20 10:10 上午
 */
@Component
public class SpringBeanTest implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, BeanPostProcessor,
        DisposableBean {

    private String name;
    private String address;

    private String beanName;

    public void setName(String name) {
        System.out.println("【注入属性】注入属性name");
        this.name = name;
    }

    public void setAddress(String address) {
        System.out.println("【注入属性】注入属性address");
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBeanName() {
        return beanName;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    /**
     * 这两个方法的第一个参数都是要处理的Bean对象，第二个参数都是Bean的name
     * 前置处理器
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if(bean instanceof com.cyb.codetest.spring.beanInit.SpringBeanTest  ) {
        System.out.println("postProcessBeforeInitialization");
        System.out.println("bean:" + bean);
        System.out.println("beanName:" + beanName);
//        }
        return bean;
    }

    /**
     * 后置处理器 在
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof com.cyb.codetest.spring.beanInit.SpringBeanTest) {
            System.out.println("postProcessAfterInitialization");
            System.out.println("bean:" + bean);
            System.out.println("beanName:" + beanName);
        }

        return bean;
    }


    public SpringBeanTest() {
        System.out.println("【构造器】调用SpringBeanTest的构造器实例化");
    }

    /**
     * setBeanName(String name) 方法的到bean的id == springBeanTest
     *
     * @param s
     */
    @Override
    public void setBeanName(String s) {
        System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName()" + s);
        this.beanName = s;
    }


    /**
     * 如果这个Bean实现了BeanFactoryAware接口，会调用它实现的setBeanFactory()，
     * 传递的是Spring工厂本身（可以用这个方法获取到其他Bean）
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        this.beanFactory = beanFactory;
    }

    /**
     * 传入Spring上下文，该方式同样可以实现步骤4，但比4更好，以为ApplicationContext是BeanFactory的子接口，
     * 有更多的实现方法
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 这是InitializingBean接口方法
     * 这一阶段也可以在bean正式构造完成前增加我们自定义的逻辑
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
    }


    /**
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("【DiposibleBean接口】调用DiposibleBean.destory()");
    }
}
