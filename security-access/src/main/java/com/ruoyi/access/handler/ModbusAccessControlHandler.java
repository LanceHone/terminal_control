package com.ruoyi.access.handler;

import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.service.IAccessCtrlInduModbusService;
//import com.ruoyi.access.service.IAccessLogService;
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
public class ModbusAccessControlHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private IAccessCtrlInduModbusService policyService;
//    private ModbusPolicyService policyService;
//    @Autowired
//    private IAccessLogService logService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ModbusFrame) {
            ModbusFrame frame = (ModbusFrame) msg;

            // 策略匹配
            boolean isAllowed = policyService.checkAccess(frame);

            // 日志记录
//            logService.logRequest(frame, isAllowed);

            if (!isAllowed) {
                ctx.writeAndFlush(buildErrorResponse(frame));
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }

    private ModbusFrame buildErrorResponse(ModbusFrame request) {
        ByteBuf data = Unpooled.buffer().writeByte(0x80 | request.getFunctionCode());
//        short functionCode = in.readUnsignedByte();
        return new ModbusFrame(
                request.getTransactionId(),
                request.getProtocolId(),
                (short) request.getUnitId(),
                (short) request.getFunctionCode(),
                data
        );
    }
}