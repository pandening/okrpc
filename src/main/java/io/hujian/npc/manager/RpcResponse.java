package io.hujian.npc.manager;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * the rpc response
 */
@Setter @Getter
public class RpcResponse {

    private String requestId; // the request id
    private String error = null; // the error desc
    private Throwable throwable; // the exception
    private Object result; // the result

    public boolean isError() {
        return error != null;
    }

}
