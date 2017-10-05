package io.hujian.npc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * FakeBeanPostProcess
 */
public class FakeBeanPostProcess implements BeanPostProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        if (o instanceof LifeCycleBean) {
            ((LifeCycleBean) o).setBeanPostProcessStr("postProcessBeforeInitialization");
            System.out.println("BeanPostProcessor.postProcessBeforeInitialization");
        }

        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {

        if (o instanceof LifeCycleBean) {

            LifeCycleBean bean = (LifeCycleBean) o;

            bean.setBeanPostProcessStr(bean.getBeanPostProcessStr() + " % postProcessAfterInitialization");

            System.out.println("BeanPostProcessor.postProcessAfterInitialization");
        }

        return o;
    }
}
