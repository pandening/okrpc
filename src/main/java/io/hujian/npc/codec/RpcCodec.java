package io.hujian.npc.codec;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * the codec of this rpc framework
 */
public interface RpcCodec {

    /**
     * enCode the object to byte array
     * @param o the origin object
     * @return the byte array
     */
    <T> byte[] enCode(T o);

    /**
     * decode object from byte array.
     * @param bytes the origin byte array
     * @param clazz the class
     * @return object from byte array
     */
    <T> Object deCode(byte[] bytes, Class<T> clazz);

}
