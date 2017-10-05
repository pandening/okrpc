package io.hujian.npc.manager;

import io.hujian.npc.codec.RpcDecoder;
import io.hujian.npc.codec.RpcEnCoder;
import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.pubisher.PublishBeanParser;
import io.hujian.npc.pubisher.RpcNodeGroup;
import io.hujian.npc.pubisher.RpcServicePublishBean;
import io.hujian.npc.register.SampleServiceRegister;
import io.hujian.npc.register.ServiceEntry;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hujian06 on 2017/10/3.
 *
 * the Service manager
 */
public class ServiceManager {
    private static final NpcLogger NPC_LOGGER = NpcLogger.getLogger(ServiceManager.class.getName());

    //thread exception handler
    private static Thread.UncaughtExceptionHandler serviceManagerExceptionHandler =
            (t, e) -> NPC_LOGGER.error("Uncaught Exception from ServiceManager. threadName = " + t.getName(), e);

    //thread factory
    private static ThreadFactory serviceManagerThreadFactory =
            new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "serviceManagerThreadPool-"
                            + threadNumber.getAndIncrement());
                    t.setUncaughtExceptionHandler(serviceManagerExceptionHandler);
                    return t;
                }
            };

    //the thread pool
    private static volatile ExecutorService serviceManagerExecutorService = Executors.newCachedThreadPool(serviceManagerThreadFactory);

    //the server manager holder
    public static class ServiceManagerHolder {
        public static final ServiceManager SERVICE_MANAGER = new ServiceManager();
    }

    private EventLoopGroup eventExecutors = new NioEventLoopGroup(16);

    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    private Map<InetSocketAddress, RpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    private AtomicInteger roundRobin = new AtomicInteger(0);
    private volatile boolean isRunning = true;

    private final PublishBeanParser publishBeanParser = PublishBeanParser.PublishBeanParserHolder.PUBLISH_BEAN_PARSER;

    private Map<String, Object> handlerMap = null; // the handler map

    private ServerBootstrap serverBootstrap; // the RpcServer BootStrap

    private Map<String, RpcServicePublishBean> publishBeanMap = null; // the bean map

    private ServiceManager() {

    }

    /**
     * assign the map, do not forget to register the service at this method.
     * @param map the handler map
     * @param bootstrap the bootstrap
     */
    public void bootRpcServer(Map<String, Object> map, ServerBootstrap bootstrap) {
        this.handlerMap = map;
        this.serverBootstrap = bootstrap;

        init();

        NPC_LOGGER.warn("Get the handler map and bootstrap, start to boot the rpc Server");

        final String[] ip_port_weight = {""};
        final String[] services = {""};

        this.publishBeanMap.forEach((service, publishBean) -> {

            String version = publishBean.getVersion();
            String serviceName = publishBean.getInterfaceName();

            RpcNodeGroup rpcNodeGroup = publishBean.getNodeGroup();

            if (rpcNodeGroup == null || rpcNodeGroup.getNodeList() == null ||
                    rpcNodeGroup.getNodeList().isEmpty()) {
                NPC_LOGGER.error("No Service Node for Service:" + service);

                return;
            }

            ip_port_weight[0] = rpcNodeGroup.getNodeList();

            String[] info = ip_port_weight[0].split(":");
            ServiceEntry serviceEntry = new ServiceEntry();

            serviceEntry.setServiceIp(info[0]);
            serviceEntry.setServicePort(info[1]);
            serviceEntry.setServiceVersion(version);
            serviceEntry.setServiceName(serviceName);

            services[0] += serviceEntry.toString() + "\n";

            SampleServiceRegister.SampleServiceRegisterHolder.SAMPLE_SERVICE_REGISTER
                    .registerService(serviceEntry);

        });

        String[] info = ip_port_weight[0].split(":");
        String host = info[0];
        int port = Integer.parseInt(info[1]);
        //open the channel
        ChannelFuture channelFuture = null;
        NPC_LOGGER.warn("Try to start RpcServer on :" + host + ":" + port + " for service:" + services[0]);
        try {
            channelFuture =
                    serverBootstrap.bind(new InetSocketAddress(host, port)).sync();

            NPC_LOGGER.warn("RpcServer start on :" + host + ":" + port + " for service:" + services[0]);
        } catch (InterruptedException e) {

            NPC_LOGGER.error("Could not start RpcServer for Service:" + services[0] + " on :" + host + ":" + port, e);
        } finally {
            if (channelFuture != null) {
                try {
                    channelFuture.channel().closeFuture().sync();

                    NPC_LOGGER.warn("Success to close the RpcServer:" + host + ":" + port);
                } catch (InterruptedException e) {

                    NPC_LOGGER.error("Could not close the Rpc Server:" + host + ":" + port + " for service:" + services[0], e);
                }
            }
        }
    }

    /**
     * parse the publish service
     */
    private void init() {
        this.publishBeanMap = publishBeanParser.getWholeServiceBeans();

        NPC_LOGGER.warn("Success to get service publish beans.[" + this.publishBeanMap.size() + "]");
    }

    /**
     * update the service
     * @param dataList the data list
     */
    public void updateService(List<String> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            Set<ServiceEntry> serviceEntrySet = new HashSet<>();

            dataList.forEach(data -> {
                ServiceEntry serviceEntry;
                String[] info = data.split("#");

                //ip-port-name-desc-tag-version
                String ip = info[0].split("=")[1];
                String port = info[1].split("=")[1];
                String name = info[2].split("=")[1];
                String desc = info[3].split("=")[1];
                String tag = info[4].split("=")[1];
                String version = info[5].split("=")[1];

                serviceEntry = new ServiceEntry(ip, port, name, desc, tag, version);

                serviceEntrySet.add(serviceEntry);

            });

            //new Service
            NPC_LOGGER.warn("Start to check if there are new Service");

            serviceEntrySet.forEach(serviceEntry -> {
                if (!connectedServerNodes.keySet().contains(serviceEntry)) {
                    connectService(serviceEntry);
                }
            });

            //need to close the old version handler
            connectedHandlers.forEach(rpcClientHandler -> {

                SocketAddress socketAddress = rpcClientHandler.getRemotePeer();

                if (!connectedHandlers.contains(socketAddress)) {

                    NPC_LOGGER.warn("Start to close the invalid handler:" + socketAddress);

                    RpcClientHandler handler = connectedServerNodes.get(socketAddress);

                    handler.close();

                    connectedServerNodes.remove(handler);

                    connectedHandlers.remove(rpcClientHandler);

                    NPC_LOGGER.warn("End of closing handler:" + socketAddress);
                }

            });

        } else { // close all of the handler
            //need to close the old version handler
            connectedHandlers.forEach(rpcClientHandler -> {

                SocketAddress socketAddress = rpcClientHandler.getRemotePeer();

                NPC_LOGGER.warn("Start to close the invalid handler:" + socketAddress);

                RpcClientHandler handler = connectedServerNodes.get(socketAddress);

                handler.close();

                connectedServerNodes.remove(handler);

                connectedHandlers.remove(rpcClientHandler);

                NPC_LOGGER.warn("End of closing handler:" + socketAddress);

            });
        }
    }

    /**
     * connect to new server
     * @param serviceEntry the entry
     */
    private void connectService(ServiceEntry serviceEntry) {
        NPC_LOGGER.warn("Start to connect to ServiceEntry:" + serviceEntry);

        serviceManagerExecutorService.submit(() -> {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline cp = socketChannel.pipeline();
                            cp.addLast("RpcEncoder", new RpcEnCoder(RpcRequest.class));
                            cp.addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
                            cp.addLast("RpcDecoder", new RpcDecoder(RpcResponse.class));
                            cp.addLast("RpcClientHandler", new RpcClientHandler());
                        }
                    });

            //get the remote address
            InetSocketAddress inetSocketAddress = new InetSocketAddress(serviceEntry.getServiceIp(),
                    Integer.parseInt(serviceEntry.getServicePort()));

            //connect to the remote address
            ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);

            //add listener
            channelFuture.addListener((ChannelFutureListener) cf -> {
                if (cf.isSuccess()) {
                    NPC_LOGGER.info("Success to connect to remote server:" + inetSocketAddress);
                    RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);

                    addHandler(handler);
                }
            });

        });
    }

    /**
     * add handler
     * @param rpcClientHandler the handler
     */
    private void addHandler(RpcClientHandler rpcClientHandler) {
        connectedHandlers.add(rpcClientHandler);
        InetSocketAddress remoteAddress = (InetSocketAddress) rpcClientHandler.getChannel().remoteAddress();
        connectedServerNodes.put(remoteAddress, rpcClientHandler);

        signalAvailableHandler();
    }

    /**
     * signalAvailableHandler
     */
    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * wait the handler
     * @return ok.
     * @throws InterruptedException e
     */
    private boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            long connectTimeoutMillis = 5000;
            return connected.await(connectTimeoutMillis, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    /**
     * stop the connection manager
     */
    public void stop(){

        NPC_LOGGER.warn("Start to Stop the Connection manager...");

        this.isRunning = false;

        for (RpcClientHandler connectedServerHandler : connectedHandlers) {
            connectedServerHandler.close();
        }

        signalAvailableHandler();

        NPC_LOGGER.warn("Shutdown the executorService...");
        serviceManagerExecutorService.shutdown();

        NPC_LOGGER.warn("Shutdown the eventGroup GraceFully...");
        eventExecutors.shutdownGracefully();
    }

    /**
     * choose a handler by roundRobin algorithm.
     * @return the handler
     */
    @SuppressWarnings(value = "unchecked")
    public RpcClientHandler robinChooseHandler() {

        CopyOnWriteArrayList<RpcClientHandler> handlers =
                (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlers.clone();

        int size = handlers.size();
        while (isRunning && size <= 0) {
            try {
                boolean available = waitingForHandler();
                if (available) {
                    handlers = (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlers.clone();
                    size = handlers.size();
                }
            } catch (InterruptedException e) {
                NPC_LOGGER.error("Interrupted when Waiting for available.", e);

                throw new RuntimeException("Interrupted when Waiting for available.", e);
            }
        }

        NPC_LOGGER.warn("RoundRobin Count:" + roundRobin.get());

        int index = (roundRobin.getAndAdd(1) + size) % size;

        return handlers.get(index);
    }


}
