package com.ruoyi.access.domain.enums;

/**
 * Modbus 协议操作类型枚举
 */
public enum OperationType {
    READ,   // 读操作
    WRITE;  // 写操作

    /**
     * 根据 Modbus 功能码获取操作类型
     * @param functionCode Modbus 功能码 (十进制表示)
     * @return 对应的操作类型
     * @throws IllegalArgumentException 如果功能码不支持
     */
    public static OperationType fromFunctionCode(int functionCode) {
        switch (functionCode) {
            // 读操作功能码
            case 1:  // 读线圈
            case 2:  // 读离散输入
            case 3:  // 读保持寄存器
            case 4:  // 读输入寄存器
                return READ;

            // 写操作功能码
            case 5:  // 写单个线圈
            case 6:  // 写单个寄存器
            case 15: // 写多个线圈
            case 16: // 写多个寄存器
                return WRITE;

            // 未知功能码处理
            default:
                throw new IllegalArgumentException(
                        "不支持的 Modbus 功能码: 0x" + Integer.toHexString(functionCode).toUpperCase()
                );
        }
    }
}