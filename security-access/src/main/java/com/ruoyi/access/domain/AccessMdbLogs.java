package com.ruoyi.access.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * modbus控制日志对象 access_mdb_logs
 *
 * @author ruoyi
 * @date 2025-04-15
 */
public class AccessMdbLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ts;

    /** 动作 */
    @Excel(name = "动作")
    private String type;

    /** tid */
    @Excel(name = "tid")
    private String tid;

    /** pid */
    @Excel(name = "pid")
    private String pid;

    /** len */
    @Excel(name = "len")
    private String len;

    /** 设备id */
    @Excel(name = "设备id")
    private String uid;

    /** 功能码 */
    @Excel(name = "功能码")
    private String func;

    /** 操作对象 */
    @Excel(name = "操作对象")
    private String addr;

    /** 操作值 */
    @Excel(name = "操作值")
    private String number;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTs(LocalDateTime ts)
    {
        this.ts = ts;
    }

    public LocalDateTime getTs()
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
    public void setTid(String tid)
    {
        this.tid = tid;
    }

    public String getTid()
    {
        return tid;
    }
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getPid()
    {
        return pid;
    }
    public void setLen(String len)
    {
        this.len = len;
    }

    public String getLen()
    {
        return len;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setFunc(String func)
    {
        this.func = func;
    }

    public String getFunc()
    {
        return func;
    }
    public void setAddr(String addr)
    {
        this.addr = addr;
    }

    public String getAddr()
    {
        return addr;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getNumber()
    {
        return number;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ts", getTs())
                .append("type", getType())
                .append("tid", getTid())
                .append("pid", getPid())
                .append("len", getLen())
                .append("uid", getUid())
                .append("func", getFunc())
                .append("addr", getAddr())
                .append("number", getNumber())
                .toString();
    }
}
