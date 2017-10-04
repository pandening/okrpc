package io.hujian.npc.manager;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * rpc request
 */
@Setter
@Getter
public class RpcRequest {

    private String requestId; // request id
    private String className; // service name
    private String methodName; // service method
    private Class<?>[] parameterTypes; // parameter types
    private Object[] parameters; // the parameters

}
