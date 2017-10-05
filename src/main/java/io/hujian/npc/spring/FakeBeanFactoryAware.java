package io.hujian.npc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * BeanFactoryAware
 */
public class FakeBeanFactoryAware implements BeanFactoryAware{

    private BeanFactory beanFactory;

    public FakeBeanFactoryAware() {
        System.out.println("+++++++ FakeBeanFactoryAware Constructor +++++++");
    }

    private void say(String what) {
        System.out.println("FakeBeanFactoryAware.say =>" + what);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

        System.out.println("BeanFactoryAware.setBeanFactory");

        FakeBeanFactoryAware beanFactoryAware = (FakeBeanFactoryAware) beanFactory.getBean("fakeBeanFactoryAware");

        beanFactoryAware.say("hello, BeanFactoryAware.setBeanFactory");
    }
}
