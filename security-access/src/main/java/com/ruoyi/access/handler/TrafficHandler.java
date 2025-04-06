//package com.ruoyi.access.handler;
//
//import com.ruoyi.access.domain.model.NetworkRequest;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.FullHttpRequest;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.List;
//
//public class TrafficHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
//
//    private final List<NetworkRequest> currentRequests;
//
//    public TrafficHandler(List<NetworkRequest> currentRequests) {
//        this.currentRequests = currentRequests;
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
//        NetworkRequest request = new NetworkRequest();
//        request.setSourceIp(ctx.channel().remoteAddress().toString());
//        request.setDestinationIp(ctx.channel().localAddress().toString());
//        request.setProtocol("HTTP"); // 可根据实际情况扩展
//        request.setData(msg.content().toString());
//
//        currentRequests.add(request);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}