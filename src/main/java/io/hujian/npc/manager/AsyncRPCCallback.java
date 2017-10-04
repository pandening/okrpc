package io.hujian.npc.manager;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * callback
 */
public interface AsyncRPCCallback {

    void success(Object result);

    void fail(Exception e);

}
