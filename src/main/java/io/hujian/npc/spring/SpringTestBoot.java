package io.hujian.npc.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * the main class
 */
public class SpringTestBoot {

    public static void main(String ... args) {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("service-publish.xml");

        LifeCycleBean bean = (LifeCycleBean) applicationContext.getBean("lifeCycleBean");

        System.out.println("Result:\n-------------------\n" + bean);
    }

}
