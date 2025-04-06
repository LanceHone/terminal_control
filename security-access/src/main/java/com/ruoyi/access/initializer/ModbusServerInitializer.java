//package com.ruoyi.access.initializer;
//
//import com.ruoyi.access.domain.model.modbus.ModbusDecoder;
//import com.ruoyi.access.handler.ModbusAccessControlHandler;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.socket.SocketChannel;
//
//public class ModbusServerInitializer extends ChannelInitializer<SocketChannel> {
//    @Override
//    protected void initChannel(SocketChannel ch) {
//        ChannelPipeline pipeline = ch.pipeline();
//
//        // 1. 解码Modbus请求
//        pipeline.addLast(new ModbusDecoder());
//
//        // 2. 访问控制拦截
//        pipeline.addLast(new ModbusAccessControlHandler());
//
//        // 3. 业务处理（若允许通过）
//        pipeline.addLast(new ModbusBusinessHandler());
//    }
//}