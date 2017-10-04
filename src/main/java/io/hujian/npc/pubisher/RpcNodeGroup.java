package io.hujian.npc.pubisher;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * node list
 */
@Setter
@Getter
public class RpcNodeGroup {

    private String nodeGroupName; // the group
    private Set<String> nodeList; // ip:port:weight

    public RpcNodeGroup() {
        nodeGroupName = "default-rpc-nodes-group"; // the default group
        nodeList = new HashSet<>();
    }

    public RpcNodeGroup(String nodeGroupName) {
        this.nodeGroupName = nodeGroupName;
        nodeList = new HashSet<>();
    }

    @Override
    public String toString() {
        if (nodeList == null || nodeList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        nodeList.forEach(node -> {
            sb.append(node).append("$");
        });

        if (sb.toString().endsWith("$")) {
            int length = sb.toString().length();
            return sb.toString().substring(0, length - 1);
        } else {
            return "[]";
        }
    }
}
