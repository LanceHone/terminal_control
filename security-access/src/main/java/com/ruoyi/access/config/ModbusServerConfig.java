//package com.ruoyi.access.config;
//
//import com.ruoyi.access.domain.model.modbus.ModbusDecoder;
//import com.ruoyi.access.handler.ModbusAccessControlHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.net.InetSocketAddress;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class ModbusServerConfig {
//    @Value("${server.port}")
//    private int appPort;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
////    private EventLoopGroup bossGroup;
////    private EventLoopGroup workerGroup;
//
//    /**
//     * TODO 将此方法封装出来，在modbus配置生效时起用此线程；禁用时关闭此线程
//     */
//
//    @PostConstruct
//    public void startServer() {
//        for (int port = 1; port <= 101; port++) {// 监听所有端口
////                for (int port = 1; port <= 65535; port++) {// 监听所有端口
//            if (port == appPort) {
//                continue;
//            }
////            if (PortUtil.isPortInUse(port)) {
////                //TODO 要记一下
////                System.out.println("Port " + port + " is in use.");
////                continue;
////            }
//            // 使用独立线程启动Netty服务器
//            int finalPort = port;
//            new Thread(() -> {
////            bossGroup = new NioEventLoopGroup();
////            workerGroup = new NioEventLoopGroup();
//
//                try {
////                ServerBootstrap b = new ServerBootstrap();
////                b.group(bossGroup, workerGroup)
////                        .channel(NioServerSocketChannel.class)
////                        .childHandler(new ChannelInitializer<SocketChannel>() {
////                            @Override
////                            protected void initChannel(SocketChannel ch) {
////                                System.out.println("已拦截到modbus请求");
////                                System.out.println("Client connected: " + ch.remoteAddress());
////                                ChannelPipeline pipeline = ch.pipeline();
////                                pipeline.addLast(new ModbusDecoder());
////                                pipeline.addLast(applicationContext.getBean(ModbusAccessControlHandler.class));
////                                System.out.println("initChannel");
////                            }
////                        });
//
////                    System.out.println("Before bind");
//                    Date startBindTime = new Date();
////                for (int port = 1; port <= 101; port++) {// 监听所有端口
//////                for (int port = 1; port <= 65535; port++) {// 监听所有端口
//                    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
//                    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//                    try {
//
//                        ServerBootstrap b = new ServerBootstrap();
//                        b.group(bossGroup, workerGroup)
//                                .channel(NioServerSocketChannel.class)
//                                .childHandler(new ChannelInitializer<SocketChannel>() {
//                                    @Override
//                                    protected void initChannel(SocketChannel ch) {
//                                        System.out.println("已拦截到modbus请求");
//                                        System.out.println("Client connected: " + ch.remoteAddress());
//                                        ChannelPipeline pipeline = ch.pipeline();
//                                        pipeline.addLast(new ModbusDecoder());
//                                        pipeline.addLast(applicationContext.getBean(ModbusAccessControlHandler.class));
//                                        System.out.println("initChannel");
//                                    }
//                                });
////                        // 配置并监听端口状态
////                        ChannelFuture f = b.bind(finalPort).addListener((ChannelFutureListener) future -> {
//////                        ChannelFuture f = b.bind(8080).addListener((ChannelFutureListener) future -> {
////                            if (future.isSuccess()) {
////                                System.out.println("Server successfully bound to port: " + finalPort);
////                            } else {
////                                System.out.println("Server failed to bind to port: " + finalPort);
////                                Throwable cause = future.cause();
////                                if (cause instanceof BindException && cause.getMessage().contains("Address already in use")) {
////                                    System.out.println("Port " + finalPort + " is already in use.");
////                                }
////                            }
////                        });
////
////                        f.sync();
//
//                        ChannelFuture f = b.bind(finalPort).sync();
//                        InetSocketAddress address = (InetSocketAddress) f.channel().localAddress();
//                        System.out.println("Server bound to port: " + address.getPort());
//                        f.channel().closeFuture().sync();
//                        System.out.println(finalPort + ":Server stopped");
//
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        shutdownGracefully(bossGroup, workerGroup);
//
//                    }
////                }
//                    Long bindDuring = System.currentTimeMillis() - startBindTime.getTime();
//                    System.out.println("bindDuring:" + bindDuring);
////                ChannelFuture f = b.bind(0).sync(); // 使用 0 表示监听所有端口
////                InetSocketAddress address = (InetSocketAddress) f.channel().localAddress();
////                System.out.println("Server bound to port: " + address.getPort());
////                f.channel().closeFuture().sync();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
////                shutdownGracefully();
//                }
//            }, "Netty-Server-Thread").start(); // 指定线程名称
//        }
//    }
//
//    //    @PreDestroy
//    public void shutdownGracefully(NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup) {
//        if (bossGroup != null) {
//            bossGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
//        }
//        if (workerGroup != null) {
//            workerGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
//        }
//    }
//}