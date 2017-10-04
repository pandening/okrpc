package io.hujian.npc.manager;

/**
 * Created by hujian06 on 2017/10/4.
 *
 *
 */
public interface IAsyncObjectProxy {
    RPCFuture call(String funcName, Object... args);
}
