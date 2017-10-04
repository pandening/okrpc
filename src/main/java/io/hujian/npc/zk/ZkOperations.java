package io.hujian.npc.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by hujian06 on 2017/10/2.
 *
 * the zookeeper operator define
 */
public interface ZkOperations {

    /**
     * get data from the zk path
     * @param path the zk path
     * @return the data of the path
     * @throws Exception throw the exception if needed
     */
    String get(String path) throws Exception;

    /**
     * get data from the path with stat.
     * @param path the zk path
     * @param stat the stat
     * @return the data of this path
     */
    String get(String path, Stat stat) throws Exception;

    /**
     * get data from the zk path with param 'isWatch'
     * @param path the zk path
     * @param isWatch watch or not
     * @return the zk data
     */
    String get(String path, boolean isWatch) throws Exception;

    /**
     * get data from the zk node.
     * @param path path
     * @param stat stat
     * @return the zk data
     */
    String getWithNodeExistsEx(String path, Stat stat) throws Exception;

    /**
     * set data to path
     * @param path the zk path
     * @param data the data
     * @param version the data's version
     */
    void set(String path, Object data, int version) throws Exception;

    /**
     * set data to path
     * @param path the path
     * @param data the data
     */
    void set(String path, Object data) throws Exception;

    /**
     * create zk node
     * @param path the node's path
     */
    void create(String path) throws Exception;

    /**
     * create the node with acl
     * @param path the path
     * @param data the data
     * @param aclList the acl list
     * @param mode the create mode
     */
    void create(String path, byte[] data, List<ACL> aclList, CreateMode mode);

    /**
     * create a zk path with data
     * @param path the path
     * @param data the data
     */
    void create(String path, Object data) throws Exception;

    /**
     * create a path with version
     * @param path the path
     * @param value the data
     * @param version the version
     */
    void create(String path, Object value, int version) throws Exception;

    /**
     * create the zk path if not exist
     * @param path the path
     * @param data the data
     */
    void createIfNotExists(String path, Object data) throws Exception;

    /**
     * create Ephemeral
     * @param path the path
     * @param data the data
     */
    void createEphemeral(String path, Object data) throws Exception;

    /**
     * createEphemeral only path
     * @param path the path
     */
    void createEphemeral(String path) throws Exception;

    /**
     * exists
     * @param path the path
     * @return true or false
     */
    boolean exists(String path) throws Exception;

    /**
     * exists
     * @param path path
     * @param watch  watch
     * @return true or false
     */
    boolean exists(String path, boolean watch) throws Exception;

    /**
     * get zk children list
     * @param path  the parent path
     * @return the child path list
     */
    List<String> getChildren(String path) throws Exception;

    /**
     * get the children and watch it
     * @param path the path
     * @param watcher the watcher
     * @return the node list
     */
    List<String> getChildren(String path, Watcher watcher);

    /**
     * get zk children list
     * @param path the path
     * @param watch is watch
     * @return the children list
     */
    List<String> getChildren(String path, boolean watch) throws Exception;

    /**
     * delete the path
     * @param path the path
     */
    void delete(String path) throws Exception;

    /**
     * delete the node
     * @param path the path
     * @param version the version
     */
    void delete(String path, int version);

    /**
     * delete if exists
     * @param path the deleted path
     */
    void deleteIfExists(String path) throws Exception;

    /**
     * watch the path
     * @param path the path
     */
    void watch(String path) throws Exception;

    /**
     * watch the path's children
     * @param path the parent path
     */
    void watchChildren(String path) throws Exception;

    /**
     * close the client
     */
    void close() throws Exception;

}
