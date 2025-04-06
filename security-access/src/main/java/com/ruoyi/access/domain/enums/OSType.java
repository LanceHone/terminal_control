package com.ruoyi.access.domain.enums;

/**
 * 操作系统类型枚举
 */
public enum OSType {
    WINDOWS, LINUX, UNKNOWN;

    /**
     * 根据操作系统名称获取OSType枚举值
     * @return OSType枚举值
     */
    public static OSType fromName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return WINDOWS;
        } else if (osName.contains("linux")) {
            return LINUX;
        } else {
            return UNKNOWN;
        }
    }

}