package com.ruoyi.access.domain.model.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ModbusDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            if (in.readableBytes() < 8) return;

            int transactionId = in.readUnsignedShort();
            int protocolId = in.readUnsignedShort();
            int length = in.readUnsignedShort();
            short unitId = in.readUnsignedByte();
            short functionCode = in.readUnsignedByte();

            // 检查剩余可读字节数是否足够
            if (in.readableBytes() < (length - 2)) {
                System.out.println("Insufficient readable bytes. Required: " + (length - 2) + ", Available: " + in.readableBytes());
                return;
            }
            ByteBuf data = in.readSlice(length - 2);

            ModbusFrame frame = new ModbusFrame(
                    transactionId, protocolId, unitId, functionCode, data
            );
            out.add(frame);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.close();
        }
    }
}