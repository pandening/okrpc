package io.hujian.npc.spring;

import org.springframework.beans.factory.BeanNameAware;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * BeanNameAware
 */
public class FakeBeanNameAware implements BeanNameAware{

    private String beanName = null;

    public FakeBeanNameAware() {

        System.out.println("----- FakeBeanNameAware Constructor ------");

        if (beanName != null) {
            System.out.println("FakeBeanNameAware.Constructor->" + beanName);
        } else {
            System.out.println("FakeBeanNameAware.Constructor shit!");
        }
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("FakeBeanNameAware.setBeanName ->" + s);

        this.beanName = s;
    }

}
