package io.hujian.npc.spring;

import org.springframework.beans.factory.DisposableBean;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * DisposableBean
 */
public class FakeDisposableBean implements DisposableBean  {

    @Override
    public void destroy() throws Exception {
        System.out.println("--->DisposableBean.destroy<-----");
    }
}
