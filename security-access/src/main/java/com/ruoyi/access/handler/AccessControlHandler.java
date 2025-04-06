package com.ruoyi.access.handler;

import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.service.IAccessCtrlNetworkTcpService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 访问控制Handler
 */
@Component
@ChannelHandler.Sharable
public class AccessControlHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private IAccessCtrlNetworkTcpService policyService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 策略匹配
        boolean isAllowed = policyService.checkAccess(msg);

        // 日志记录
//            logService.logRequest(msg, isAllowed);

        if (!isAllowed) {
            ctx.writeAndFlush(buildErrorResponse(msg));
            return;
        }
        ctx.fireChannelRead(msg);
    }













    private Object buildErrorResponse(Object request) {
        return request;
    }
}