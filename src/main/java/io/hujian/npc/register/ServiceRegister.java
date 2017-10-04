package io.hujian.npc.register;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * the service register
 */
public interface ServiceRegister {

    /**
     * register this service to zookeeper server. let the client could
     * find the server.
     * @param serviceEntry the service entry
     */
    void registerService(ServiceEntry serviceEntry);

}
