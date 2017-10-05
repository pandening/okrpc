package io.hujian.npc.spring;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/5.
 *
 * the base bean
 */
@Setter
@Getter
public class BaseBean {

    private String desc;

    public BaseBean(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BaseBean[" + desc + "]";
    }

    public void init() {
        System.out.println("BaseBean init!");
    }

    public void destroy() {
        System.out.println("BaseBean destroy!");
    }
}
