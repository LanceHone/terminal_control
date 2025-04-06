package com.ruoyi.access.domain.model.modbus;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * Modbus帧
 */
public class ModbusFrame implements Serializable {
    private int transactionId;
    private int protocolId;
    private int unitId;
    private int functionCode;
    private ByteBuf data;

    public ModbusFrame(int transactionId, int protocolId, short unitId, short functionCode, ByteBuf data) {
        this.transactionId = transactionId;
        this.protocolId = protocolId;
        this.unitId = unitId;
        this.functionCode = functionCode;
        this.data = data;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(int functionCode) {
        this.functionCode = functionCode;
    }

    public ByteBuf getData() {
        return data;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ModbusFrame{" +
                "transactionId=" + transactionId +
                ", protocolId=" + protocolId +
                ", unitId=" + unitId +
                ", functionCode=" + functionCode +
                '}';
    }
}