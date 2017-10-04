package io.hujian.npc.pubisher;


/**
 * Created by hujian06 on 2017/10/4.
 *
 * serialize type choose
 */
public enum SerializeType {
    PROTOSTUFF(0, "protostuff");

    SerializeType(int id, String desc) {
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
                return c.desc;
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
            if (c.getDesc() == desc) {
                return c.id;
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
