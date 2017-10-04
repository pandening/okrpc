package io.hujian.npc.manager;

import io.hujian.npc.codec.RpcDecoder;
import io.hujian.npc.codec.RpcEnCoder;
import io.hujian.npc.logger.NpcLogger;
import io.hujian.npc.register.ServiceEntry;
import io.hujian.npc.register.ServiceRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hujian06 on 2017/10/4.
 *
 * Rpc Server
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final NpcLogger LOGGER = NpcLogger.getLogger(RpcServer.class.getName());

    private String serverAddress;
    private ServiceRegister serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<>();

    private static volatile ThreadPoolExecutor threadPoolExecutor;

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcServer(String serverAddress, ServiceRegister serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName, serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536,
                                            0,4,0,
                                            0))
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEnCoder(RpcResponse.class))
                                    .addLast(new RpcServerHandler(handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ServiceManager.ServiceManagerHolder.SERVICE_MANAGER.assignHandlerMap(handlerMap);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();

            LOGGER.warn("Rpc Server Start on :" + host + ":" + port);

            if (serviceRegistry != null) {
                ServiceEntry serviceEntry = new ServiceEntry();
                serviceEntry.setServiceIp(host);
                serviceEntry.setServicePort(String.valueOf(port));

                LOGGER.warn("Just ip and port to Register.");

                serviceRegistry.registerService(serviceEntry);
            }

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * submit the task
     * @param task the task
     */
    public static void submit(Runnable task){
        if(threadPoolExecutor == null){
            synchronized (RpcServer.class) {
                if(threadPoolExecutor == null){
                    threadPoolExecutor =
                            new ThreadPoolExecutor(16, 16,
                                    600L, TimeUnit.SECONDS,
                                    new ArrayBlockingQueue<Runnable>(65536));
                }
            }
        }

        threadPoolExecutor.submit(task);
    }

}
