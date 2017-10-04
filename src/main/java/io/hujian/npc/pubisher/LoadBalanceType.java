package io.hujian.npc.pubisher;

import java.util.Objects;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * the load balance
 */
public enum LoadBalanceType {
    ROUND_ROBIN(0, "roundRobin"), // round robin -> default
    WEIGHT_TYPE(1,"weight"), // according to the weight
    RANDOM_TYPE(2,"random"); // random.

    LoadBalanceType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    private int id;
    private String desc;

    /**
     * get the desc according to the index
     * @param index the index
     * @return the desc
     */
    public static String getDesc(int index) {
        for (SerializeType c : SerializeType.values()) {
            if (c.getId() == index) {
                return c.getDesc();
            }
        }
        return null;
    }

    /**
     * get the index according to the desc
     * @param desc the desc
     * @return the index
     */
    public static int getIndex(String desc) {
        for (SerializeType c : SerializeType.values()) {
            if (Objects.equals(c.getDesc(), desc)) {
                return c.getId();
            }
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
