package io.hujian.npc.pubisher;

import io.hujian.npc.logger.NpcLogger;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * the bean.
 */
@Setter
@Getter
public class RpcServicePublishBean {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(RpcServicePublishBean.class.getName());

    private String url; // the service url, ensure unique.
    private String interfaceName; // the interface
    private String serialize; // the serialize method.
    private String callType; // the callType
    private int retries; // try time
    private int timeout; // the timeout value (ms)
    private String version; // version

    private RpcNodeGroup nodeGroup; // the nodeGroup.
    private String loadBalance; // loadBalance

    public RpcServicePublishBean() {
        serialize = SerializeType.PROTOSTUFF.getDesc(); // this is the default
        callType = CallType.SYNC.getDesc(); // sync
        loadBalance = LoadBalanceType.ROUND_ROBIN.getDesc();// round robin.
        retries = 1; // 1 time
        timeout = 4000; // 4s

        NPC_LOGGER.info("constructor of RpcServicePublishBean run.");
    }

    @Override
    public String toString() {
        return url + "_" + interfaceName + "_" + version + "_" + callType +
                "_" + serialize + "_" + retries + "_" + timeout + "_" + nodeGroup.toString();
    }

}
