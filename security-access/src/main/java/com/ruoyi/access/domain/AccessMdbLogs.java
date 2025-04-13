package com.ruoyi.access.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * modbus控制日志对象 access_mdb_logs
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
public class AccessMdbLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private String ts;

    /** 访问地址 */
    @Excel(name = "设备id")
    private String device;


    public AccessMdbLogs(String timestamp, String u, String f, String addr, String num) {
        this.ts = timestamp;
        this.device = u;
        this.value = num;
        this.addr = addr;
        this.type = f;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    /** 类型 */
    @Excel(name = "功能码")
    private String type;

    /** 访问地址 */
    @Excel(name = "访问地址")
    private String addr;

    @Excel(name = "读写")
    private String operate;
    @JsonProperty("rw")
    public String getOperate()
    {
        return Integer.parseInt(type) < 5 ? "读" : "写";
    }

    /** 写入值 */
    @Excel(name = "写入值")
    private String value;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTs(String ts) 
    {
        this.ts = ts;
    }

    public String getTs() 
    {
        return ts;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setAddr(String addr) 
    {
        this.addr = addr;
    }

    public String getAddr() 
    {
        return addr;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ts", getTs())
            .append("type", getType())
            .append("addr", getAddr())
            .append("value", getValue())
            .toString();
    }
}
