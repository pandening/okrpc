package io.hujian.npc.pubisher;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * node list
 */
@Setter
@Getter
public class RpcNodeGroup {

    private String nodeGroupName; // the group
    private String nodeList; // ip:port:weight

    public RpcNodeGroup() {
        nodeGroupName = "default-rpc-nodes-group"; // the default group
    }

    public RpcNodeGroup(String nodeGroupName) {
        this.nodeGroupName = nodeGroupName;
    }

    @Override
    public String toString() {
        if (nodeList == null || nodeList.isEmpty()) {
            return "";
        }

        return "[" + nodeGroupName + "] -> [" + nodeList + "]";
    }
}
