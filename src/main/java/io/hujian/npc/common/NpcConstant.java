package io.hujian.npc.common;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * some const here
 */
public class NpcConstant {

    public static final int ZK_RETRIES = 10;
    public static final int ZK_RETRY_RETRY_INTERVAL = 1000;
    public static final int ZK_RETRY_LIMIT = 20;
    public static final int ZK_SESSION_TIME_OUT = 5000;
    public static final int ZK_CONNECTION_TIME_OUT = 5000;
    public static final String  ZK_SERVER_ADDRESS = "127.0.0.1:2181";

    public static final String NPC_SERVICE_REGISTER_PATH = "/npc_service_register";
    public static final String NPC_SERVICE_DATA_PATH = NPC_SERVICE_REGISTER_PATH + "/data";

}
