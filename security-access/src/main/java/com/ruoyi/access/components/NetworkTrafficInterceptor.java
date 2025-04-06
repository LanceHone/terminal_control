//package com.ruoyi.access.components;
//
//import com.ruoyi.access.domain.model.NetworkRequest;
//import com.ruoyi.access.handler.TrafficHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//public class NetworkTrafficInterceptor {
//
//    private final List<NetworkRequest> currentRequests = new CopyOnWriteArrayList<>();
//    private ServerBootstrap serverBootstrap;
//
//    public void start(int port) {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        serverBootstrap = new ServerBootstrap();
//        serverBootstrap.group(bossGroup, workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new HttpServerCodec()); // 使用HTTP协议
//                        pipeline.addLast(new HttpObjectAggregator(65536));
//                        pipeline.addLast(new TrafficHandler(currentRequests)); // 自定义流量处理逻辑
//                    }
//                });
//
//        try {
//            ChannelFuture future = serverBootstrap.bind(port).sync();
//            System.out.println("网络流量捕获器已启动，监听端口：" + port);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    public List<NetworkRequest> getCurrentRequests() {
//        return currentRequests;
//    }
//
//    public void stop() {
//        serverBootstrap.group().shutdownGracefully();
//        serverBootstrap.childGroup().shutdownGracefully();
//    }
//}