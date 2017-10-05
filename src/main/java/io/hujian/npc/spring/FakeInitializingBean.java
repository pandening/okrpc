package io.hujian.npc.spring;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * InitializingBean
 */
public class FakeInitializingBean implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean.afterPropertiesSet");
    }
}
