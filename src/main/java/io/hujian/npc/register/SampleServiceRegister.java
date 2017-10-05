package io.hujian.npc.register;

import io.hujian.npc.common.NpcConstant;
import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.zk.ZookeeperClient;

import java.util.Objects;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * implement of serviceRegister
 */
public class SampleServiceRegister implements ServiceRegister{
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(SampleServiceRegister.class.getName());

    public static class SampleServiceRegisterHolder {
        public static final SampleServiceRegister SAMPLE_SERVICE_REGISTER = new SampleServiceRegister();
    }

    private static final ZookeeperClient ZOOKEEPER_CLIENT = ZookeeperClient.ZookeeperClientFactory.sharedZkClient();

    private static final String SERVICE_REGISTER_PATH = NpcConstant.NPC_SERVICE_REGISTER_PATH;
    private static final String SERVICE_DATA_PATH = NpcConstant.NPC_SERVICE_DATA_PATH;

    /**
     * trans the service entry to string
     * @param entry the service entry
     * @return string value
     */
    private String generateServiceData(ServiceEntry entry) {
        if (entry == null) {
            return "";
        }

        return "ip=" + entry.getServiceIp() + "#port=" + entry.getServicePort() +
                "#sname=" + entry.getServiceName() + "#sdesc=" + entry.getServiceDesc() +
                "#stag=" + entry.getServiceTag() + "#sversion=" + entry.getServiceVersion();
    }

    /**
     * check and create the register if needed
     */
    private void createServiceRegisterRootNode() {
        try {
            if (!ZOOKEEPER_CLIENT.exists(SERVICE_REGISTER_PATH)) {
                ZOOKEEPER_CLIENT.create(SERVICE_REGISTER_PATH);

                NPC_LOGGER.warn("Success to create the service register root node.");
            }

            if (!ZOOKEEPER_CLIENT.exists(SERVICE_DATA_PATH)) {
                ZOOKEEPER_CLIENT.create(SERVICE_DATA_PATH);

                NPC_LOGGER.warn("Success to create the service register data node.");
            }

        } catch (Exception e) {

            NPC_LOGGER.error("Error When using exists", e);
        }
    }

    @Override
    public void registerService(ServiceEntry serviceEntry) {
        String data = generateServiceData(serviceEntry);

        NPC_LOGGER.error("Start to Register:" + data);

        if (Objects.equals("", data)) {
            NPC_LOGGER.error("Empty ServiceEntry.");
        } else {

            //create the root node.
            createServiceRegisterRootNode();

            try {
                ZOOKEEPER_CLIENT.create(SERVICE_DATA_PATH + "/" + data, data);

                NPC_LOGGER.warn("Success to register the service:" + data + " to zookeeper Server");
            } catch (Exception e) {

                NPC_LOGGER.error("Could not register the service:" + data + " to zookeeper server", e);
            }
        }
    }
}
