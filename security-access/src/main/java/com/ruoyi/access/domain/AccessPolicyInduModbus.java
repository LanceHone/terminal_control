package com.ruoyi.access.domain;

import com.ruoyi.common.annotation.Excel;

/**
 * MODBUS网络控制对象
 */
// AccessCtrlModbus.java
//@Data
//@TableName("access_ctrl_modbus")
public class AccessPolicyInduModbus extends AccessPolicy {
    private static final long serialVersionUID = 1L;

    private Long id;
    // 设备ID
    private String deviceId;
    // 功能码
    private String functionCode;
    // 读写
    private String rw;
    // 寄存器地址
    private String registerAddress;
    // 控制值范围
    private String valueRange;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getValueRange() {
        return valueRange;
    }

    public void setValueRange(String valueRange) {
        this.valueRange = valueRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}