package io.hujian.npc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * InstantiationAwareBeanPostProcessor (father)
 *
 * InstantiationAwareBeanPostProcessorAdapter(child)
 */
public class FakeInstantiationAwareBeanPostProcess
        extends InstantiationAwareBeanPostProcessorAdapter {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (beanName.equals("lifeCycleBean")) {
            System.out.println("InstantiationAwareBeanPostProcessorAdapter.postProcessBeforeInstantiation");
        }

        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        if (bean instanceof LifeCycleBean) {
            System.out.println("InstantiationAwareBeanPostProcessorAdapter.postProcessAfterInstantiation");
        }

        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        if (bean instanceof LifeCycleBean) {
            System.out.println("InstantiationAwareBeanPostProcessorAdapter.postProcessPropertyValues");
        }

        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof LifeCycleBean) {
            System.out.println("InstantiationAwareBeanPostProcessorAdapter.postProcessBeforeInitialization");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof LifeCycleBean) {
            System.out.println("InstantiationAwareBeanPostProcessorAdapter.postProcessAfterInitialization");
        }

        return bean;
    }

}
