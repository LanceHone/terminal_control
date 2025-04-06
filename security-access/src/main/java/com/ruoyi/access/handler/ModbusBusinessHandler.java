//package com.ruoyi.access.handler;
//
//import com.ruoyi.access.domain.model.modbus.ModbusFrame;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.logging.Logger;
//
//@Component
//@ChannelHandler.Sharable
//public class ModbusBusinessHandler extends ChannelInboundHandlerAdapter {
//    private static final Logger logger = LoggerFactory.getLogger(ModbusBusinessHandler.class);
//
//    @Autowired
//    private DeviceConnectionManager connectionManager;
//
//    @Value("${modbus.simulation.enabled:false}")
//    private boolean simulationEnabled;
//
//    /**
//     * 处理合法通过的Modbus请求
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if (!(msg instanceof ModbusFrame)) {
//            ctx.fireChannelRead(msg); // 传递非Modbus协议数据
//            return;
//        }
//
//        ModbusFrame request = (ModbusFrame) msg;
//        try {
//            if (simulationEnabled) {
//                // 模拟模式：直接返回测试数据
//                ModbusFrame simulatedResponse = simulateDeviceResponse(request);
//                ctx.writeAndFlush(simulatedResponse);
//            } else {
//                // 实际设备通信
//                forwardToPhysicalDevice(request, ctx);
//            }
//        } catch (Exception e) {
//            logger.error("业务处理异常: {}", e.getMessage());
//            sendExceptionResponse(ctx, request, 0x01); // 返回非法功能码异常
//        }
//    }
//
//    /**
//     * 转发请求到物理设备
//     */
//    private void forwardToPhysicalDevice(ModbusFrame request, ChannelHandlerContext clientCtx) {
//        int deviceId = request.getUnitId();
//        Channel deviceChannel = connectionManager.getDeviceChannel(deviceId);
//
//        if (deviceChannel == null || !deviceChannel.isActive()) {
//            logger.warn("设备 {} 未连接", deviceId);
//            sendExceptionResponse(clientCtx, request, 0x0A); // 设备离线异常码
//            return;
//        }
//
//        // 转发请求到设备通道
//        deviceChannel.writeAndFlush(request).addListener(future -> {
//            if (!future.isSuccess()) {
//                logger.error("请求转发失败", future.cause());
//                sendExceptionResponse(clientCtx, request, 0x05); // 写操作失败异常
//            }
//        });
//
//        // 监听设备响应
//        deviceChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) {
//                if (msg instanceof ModbusFrame) {
//                    ModbusFrame response = (ModbusFrame) msg;
//                    clientCtx.writeAndFlush(response); // 将设备响应返回客户端
//                    ctx.pipeline().remove(this); // 移除临时监听器
//                }
//            }
//        });
//    }
//
//    /**
//     * 模拟设备响应（测试用）
//     */
//    private ModbusFrame simulateDeviceResponse(ModbusFrame request) {
//        ByteBuf data;
//        switch (request.getFunctionCode()) {
//            case 3: // 读保持寄存器
//                data = Unpooled.buffer(4)
//                        .writeShort(0x0003) // 寄存器地址
//                        .writeShort(0x00FF); // 模拟值 255
//                break;
//            case 6: // 写单个寄存器
//                data = Unpooled.copyShort(request.getData().getShort(0)); // 回显写入地址
//                break;
//            default:
//                data = Unpooled.EMPTY_BUFFER;
//        }
//        return new ModbusFrame(
//                request.getTransactionId(),
//                request.getProtocolId(),
//                request.getUnitId(),
//                request.getFunctionCode(),
//                data
//        );
//    }
//
//    /**
//     * 构造异常响应帧
//     */
//    private void sendExceptionResponse(ChannelHandlerContext ctx, ModbusFrame request, int errorCode) {
//        ByteBuf data = Unpooled.buffer()
//                .writeByte(request.getFunctionCode() | 0x80) // 异常功能码
//                .writeByte(errorCode); // 异常码
//
//        ModbusFrame errorFrame = new ModbusFrame(
//                request.getTransactionId(),
//                request.getProtocolId(),
//                request.getUnitId(),
//                data
//        );
//        ctx.writeAndFlush(errorFrame);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        logger.error("通信管道异常: {}", cause.getMessage());
//        ctx.close();
//    }
//}
//
