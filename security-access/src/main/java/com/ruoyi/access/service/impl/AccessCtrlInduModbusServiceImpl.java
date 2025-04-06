package com.ruoyi.access.service.impl;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.enums.OperationType;
import com.ruoyi.access.domain.model.modbus.ModbusDecoder;
import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.handler.ModbusAccessControlHandler;
import com.ruoyi.access.mapper.AccessCtrlInduModbusMapper;
import com.ruoyi.access.service.IAccessCtrlInduModbusService;
import com.ruoyi.access.util.AccessPolicyUtil;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 工业Modbus服务层处理
 */
@Service
public class AccessCtrlInduModbusServiceImpl implements IAccessCtrlInduModbusService {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccessCtrlInduModbusMapper modbusMapper;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Override
    @DataScope
    public List<AccessPolicyInduModbus> selectModbusList(AccessPolicyInduModbus modbus) {
        return modbusMapper.selectList(modbus);
//        return modbusMapper.selectModbusList(modbus);
    }

    @Override
    public AccessPolicyInduModbus selectModbusById(Long id) {
        return modbusMapper.selectById(id);
    }

    @Override
    @Transactional
    public int insertModbus(AccessPolicyInduModbus modbus) {
        // 唯一性校验
        AccessPolicyInduModbus exist = modbusMapper.checkDeviceRegisterUnique(
                modbus.getDeviceId(), modbus.getRegisterAddress());
        if (exist != null) {
            throw new ServiceException("设备ID[" + modbus.getDeviceId() +
                    "]与寄存器地址[" + modbus.getRegisterAddress() + "]已存在");
        }
        // 设置状态为禁用
        modbus.setStatus("1");
        modbus.setCreateBy(SecurityUtils.getUsername());
        return modbusMapper.insert(modbus);
//        // 唯一性校验
//        AccessPolicyInduModbus exist = modbusMapper.checkDeviceRegisterUnique(
//                modbus.getDeviceId(), modbus.getRegisterAddress());
//        if (exist != null) {
//            throw new ServiceException("设备ID[" + modbus.getDeviceId() +
//                    "]与寄存器地址[" + modbus.getRegisterAddress() + "]已存在");
//        }
//        modbus.setCreateBy(SecurityUtils.getUsername());
//        return modbusMapper.insert(modbus);
    }

    @Override
    @Transactional
    public int updateModbus(AccessPolicyInduModbus modbus) {
        // 更新时校验唯一性（排除自身）
        AccessPolicyInduModbus exist = modbusMapper.checkDeviceRegisterUnique(
                modbus.getDeviceId(), modbus.getRegisterAddress());
        if (exist != null && !exist.getId().equals(modbus.getId())) {
            throw new ServiceException("设备ID与寄存器地址组合冲突");
        }
        modbus.setUpdateBy(SecurityUtils.getUsername());
        return modbusMapper.update(modbus);
    }

    @Override
    @Transactional
    public int deleteModbusByIds(Long[] ids) {
        return modbusMapper.deleteByIds(ids);
    }

    @Override
    public String checkDeviceRegisterUnique(AccessPolicyInduModbus modbus) {
        AccessPolicyInduModbus info = modbusMapper.checkDeviceRegisterUnique(
                modbus.getDeviceId(), modbus.getRegisterAddress());
        if (info != null && !info.getId().equals(modbus.getId())) {
            return "1"; // 存在冲突
        }
        return "0"; // 唯一
    }

    @Override
    @Transactional
    public int updateModbusStatus(AccessPolicyInduModbus modbus) {
        if (modbus.getStatus() == "1") {
            //停止相应线程
        } else if (modbus.getStatus() == "0") {
            //启动相应线程
        }
        int ret = modbusMapper.updateModbusStatus(modbus);
        return ret;
    }

    @Override
    public boolean checkAccess(ModbusFrame frame) {
        List<AccessPolicyInduModbus> policies = getActivePolicies();
//        int registerAddr = parseRegister(frame);
        int registerAddr = 4553;
        Integer value = parseValue(frame);
//        Integer value = parseValue(frame.getData());

        String check = policies.stream()
                .filter(p -> matchesPolicy(p, frame, registerAddr, value))
                .findFirst()
                .map(AccessPolicyInduModbus::getStatus)
                .orElse("1"); // 默认拒绝
        return StringUtils.equals(check, "0");
    }

    @Override
    public Thread startServer() {
        Thread policythread = new Thread(() -> {
//            bossGroup = new NioEventLoopGroup();
//            workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                System.out.println("已拦截到modbus请求");
                                System.out.println("Client connected: " + ch.remoteAddress());
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new ModbusDecoder());
                                pipeline.addLast(applicationContext.getBean(ModbusAccessControlHandler.class));
                                System.out.println("initChannel");
                            }
                        });

                System.out.println("Before bind");
                ChannelFuture f = b.bind(0).sync(); // 使用 0 表示监听所有端口
                InetSocketAddress address = (InetSocketAddress) f.channel().localAddress();
                System.out.println("Server bound to port: " + address.getPort());
                f.channel().closeFuture().sync();
                System.out.println("Server stopped");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                shutdownGracefully();
            }
        }, "Netty-Server-Thread");// 指定线程名称
        policythread.start();
        return policythread;
    }

    public void shutdownGracefully() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully(0, 5, TimeUnit.SECONDS);
        }
    }

    private int parseRegister(ModbusFrame frame) {
        // 获取当前功能码（假设已从ModbusFrame中获取）
        int functionCode = frame.getFunctionCode();

        // 检查数据是否足够解析寄存器地址
        ByteBuf data = frame.getData();
        if (data.readableBytes() < 2) {
            throw new IllegalArgumentException("数据长度不足以解析寄存器地址");
        }

        int registerAddr;

        switch (functionCode) {
            case 3:  // 读保持寄存器 (03)
            case 6:  // 写单个寄存器 (06)
            case 16: // 写多个寄存器 (16)
                // 寄存器地址位于数据的前两个字节（大端序）
                registerAddr = data.getUnsignedShort(0);
                break;

            case 1:  // 读线圈 (01)
            case 2:  // 读离散输入 (02)
            case 4:  // 读输入寄存器 (04)
                // 寄存器地址同样位于前两个字节
                registerAddr = data.getUnsignedShort(0);
                break;

            case 5:  // 写单个线圈 (05)
            case 15: // 写多个线圈 (15)
                // 线圈地址解析（虽然逻辑不同，但地址仍在前两个字节）
                registerAddr = data.getUnsignedShort(0);
                break;

            default:
                throw new UnsupportedOperationException("不支持的功能码: " + functionCode);
        }

        return registerAddr;
    }

    private Integer parseValue(ModbusFrame frame) {
        int functionCode = frame.getFunctionCode();

        ByteBuf data = frame.getData();
        // 仅写操作需要解析值
        switch (functionCode) {
            case 6:  // 写单个寄存器
                // 值位于第3-4字节（大端序）
                if (data.readableBytes() < 4) {
                    throw new IllegalArgumentException("数据长度不足以解析值");
                }
                return data.getUnsignedShort(2);

            case 16: // 写多个寄存器
                // 起始地址后是寄存器数量，值位于后续字节（需根据数量解析）
                int numRegisters = data.getUnsignedShort(2);
                // 示例仅返回第一个寄存器的值（实际需完整解析）
                return data.getUnsignedShort(4);

            case 5:  // 写单个线圈
                // 值为0xFF00（ON）或0x0000（OFF）
                return data.getUnsignedShort(2) == 0xFF00 ? 1 : 0;

            default:
                return null; // 非写操作返回null
        }
    }

//    @Transactional
//    public void updatePolicies(List<AccessPolicyInduModbus> newPolicies) {
//        // 冲突检测逻辑
////        modbusMapper.saveAll(newPolicies);
//    }

    private boolean matchesPolicy(
            AccessPolicyInduModbus policy,
            ModbusFrame frame,
            Integer registerAddr,
            Integer value) {
        // 1. 设备ID匹配（策略中deviceId为null时匹配所有设备）
        if (policy.getDeviceId() != null &&
                !policy.getDeviceId().equals(String.valueOf(frame.getUnitId()))) {
            return false;
        }

        // 2. 功能码匹配（策略中functionCode为null时匹配所有功能码）
        if (policy.getFunctionCode() != null &&
                !policy.getFunctionCode().equals(String.valueOf(frame.getFunctionCode()))) {
//                policy.getFunctionCode() != frame.getFunctionCode()) {
            return false;
        }

        // 3. 寄存器地址范围检查
        Integer[] registerRange = AccessPolicyUtil.rangeSplit(policy.getRegisterAddress());
        if (registerRange[0] != null &&
                registerAddr.compareTo(registerRange[0]) < 0) {
            return false;
        }
        if (registerRange[1] != null &&
                registerAddr.compareTo(registerRange[1]) > 0) {
            return false;
        }

        // 4. 操作类型校验
        OperationType requestOpType = getOperationType(frame.getFunctionCode());
//        if (policy.getRw() != null &&
//                policy.getRw() != requestOpType) {
//            return false;
//        }

        // 5. 写操作值范围校验（仅当操作类型为WRITE时生效）
        if (requestOpType == OperationType.WRITE) {
            if (value == null) return false; // 写操作必须包含有效值
            Integer[] valueRange = AccessPolicyUtil.rangeSplit(policy.getValueRange());
            if (valueRange[0] != null &&
                    value < valueRange[0]) {
                return false;
            }
            if (valueRange[1] != null &&
                    value > valueRange[1]) {
                return false;
            }
        }

        // 所有条件满足
        return true;
    }

    // 辅助方法：根据功能码判断操作类型
    private OperationType getOperationType(int functionCode) {
        switch (functionCode) {
            case 1:  // 读线圈
            case 2:  // 读离散输入
            case 3:  // 读保持寄存器
            case 4:  // 读输入寄存器
                return OperationType.READ;

            case 5:  // 写单个线圈
            case 6:  // 写单个寄存器
            case 15: // 写多个线圈
            case 16: // 写多个寄存器
                return OperationType.WRITE;

            default:
                throw new IllegalArgumentException("未知功能码: " + functionCode);
        }
    }

    //    @Cacheable("modbusPolicies")
    public List<AccessPolicyInduModbus> getActivePolicies() {
        return modbusMapper.findAllActive();
    }


    @Override
    public String checkAccessRequest(AccessPolicyInduModbus modbus) {
//    public String checkAccessRequest(Long deviceId, String functionCode, String rw, Integer registerAddress, Integer valueRange) {
        // 根据设备ID、功能码和读写类型查询对应的访问策略
        AccessPolicyInduModbus accessPolicy = modbusMapper.checkAccess(modbus.getDeviceId(), modbus.getFunctionCode(), modbus.getRw());

        if (accessPolicy != null && "0".equals(accessPolicy.getStatus())) {
            // 寄存器地址和控制值范围的校验
            String registerRange = accessPolicy.getRegisterAddress();
            String valueRangePolicy = accessPolicy.getValueRange();

            // 解析寄存器地址范围
            List<Integer> registerLimits = parseRange(registerRange);
            if (registerLimits == null || Integer.valueOf(modbus.getRegisterAddress()) < registerLimits.get(0) || Integer.valueOf(modbus.getRegisterAddress() )> registerLimits.get(1)) {
                return "访问受限";
            }

            // 解析控制值范围
            List<Integer> valueLimits = parseRange(valueRangePolicy);
            if (valueLimits == null || Integer.valueOf(modbus.getValueRange()) < valueLimits.get(0) || Integer.valueOf(modbus.getValueRange()) > valueLimits.get(1)) {
                return "访问受限";
            }

            // 如果所有条件都满足，返回“正常通信”和一个随机值
            return "正常通信，随机值：" + new Random().nextInt(1000);
        }

        // 如果没有找到匹配的策略或者状态不是“正常”，返回“访问受限”
        return "访问受限";
    }

    private List<Integer> parseRange(String range) {
        if (range == null || !range.contains("～")) {
            return null;
        }
        String[] limits = range.split("～");
        if (limits.length != 2) {
            return null;
        }
        try {
            int lower = Integer.parseInt(limits[0].trim());
            int upper = Integer.parseInt(limits[1].trim());
            return Arrays.asList(lower, upper);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Boolean enablePolicy(Long policyId) {
        return null;
    }

    @Override
    public Boolean disablePolicy(Long policyId) {
        return null;
    }
}