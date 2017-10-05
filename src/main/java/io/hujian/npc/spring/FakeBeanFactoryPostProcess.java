package io.hujian.npc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * the BeanFactoryPostProcess
 */
public class FakeBeanFactoryPostProcess implements BeanFactoryPostProcessor{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        BaseBean baseBean = new BaseBean("bfPostBaseBean");

        configurableListableBeanFactory.registerSingleton("bfPostBaseBean", baseBean);

        System.out.println("BeanFactoryPostProcessor -> bfPostBaseBean");
    }
}
