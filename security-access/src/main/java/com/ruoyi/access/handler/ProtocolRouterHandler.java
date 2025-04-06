//package com.ruoyi.access.handler;
//
//import com.ruoyi.access.domain.model.modbus.ModbusDecoder;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.ByteToMessageDecoder;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//
//import java.util.List;
//
//public class ProtocolRouterHandler extends ByteToMessageDecoder {
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
//        // 识别协议类型
//        if (isHttpRequest(in)) {
//            out.add(new HttpObjectAggregator(65536).decode(ctx, in)); // 转发给协议处理器
//        }  else {
//            ctx.close(); // 未知协议直接关闭连接
//        }
//    }
//
//    private boolean isHttpRequest(ByteBuf buf) {
//        return buf.getByte(0) == 'G' && buf.getByte(1) == 'E' && buf.getByte(2) == 'T';
//    }
//}