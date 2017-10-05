package io.hujian.npc.spring;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * test spring lifecycle
 */
@Setter
@Getter
public class LifeCycleBean {

    private BaseBean baseBean; // test base bean
    private BaseBean beanFactoryPostProcessBaseBean; // test the BeanFactoryPostProcess

    private String desc;
    private String beanPostProcessStr; // test the BeanPostProcessor

    public LifeCycleBean() {
        System.out.println("!!!!!!!!! LifeCycleBean's Constructor !!!!!!!!!");
    }

    public LifeCycleBean(BaseBean baseBean, String desc) {
        this.baseBean = baseBean;
        this.desc = desc;
    }

    public void init() {
        System.out.println("LifeCycleBean init!");
    }

    public void destroy() {
        System.out.println("LifeCycleBean destroy!");
    }

    @Override
    public String toString() {
        return "baseBean:" + baseBean.toString() + "\nbeanFactoryPostProcessBaseBean:" +
                beanFactoryPostProcessBaseBean.toString() + "\nbeanPostProcessStr:" +
                beanPostProcessStr + "\ndesc:" + desc;
    }

}
