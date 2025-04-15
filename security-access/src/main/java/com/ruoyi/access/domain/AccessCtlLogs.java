package com.ruoyi.access.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 访问控制日志对象 access_ctl_logs
 *
 * @author ruoyi
 * @date 2025-04-15
 */
public class AccessCtlLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 源IP */
    @Excel(name = "源IP")
    private String src;

    /** 目标IP */
    @Excel(name = "目标IP")
    private String dst;

    /** 源端口 */
    @Excel(name = "源端口")
    private String spt;

    /** 目标端口 */
    @Excel(name = "目标端口")
    private String dpt;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ts;

    /** 协议 */
    @Excel(name = "协议")
    private String protocol = "tcp";

    /** 动作 */
    @Excel(name = "动作")
    private String action;

    /** 模块 */
    @Excel(name = "模块")
    private String module;

    /** 主机 */
    @Excel(name = "主机")
    private String host;

    /** mac */
    @Excel(name = "mac")
    private String mac;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSrc(String src)
    {
        this.src = src;
    }

    public String getSrc()
    {
        return src;
    }
    public void setDst(String dst)
    {
        this.dst = dst;
    }

    public String getDst()
    {
        return dst;
    }
    public void setSpt(String spt)
    {
        this.spt = spt;
    }

    public String getSpt()
    {
        return spt;
    }
    public void setDpt(String dpt)
    {
        this.dpt = dpt;
    }

    public String getDpt()
    {
        return dpt;
    }
    public void setTs(LocalDateTime ts)
    {
        this.ts = ts;
    }

    public LocalDateTime getTs()
    {
        return ts;
    }
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getProtocol()
    {
        return protocol;
    }
    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return action;
    }
    public void setModule(String module)
    {
        this.module = module;
    }

    public String getModule()
    {
        return module;
    }
    public void setHost(String host)
    {
        this.host = host;
    }

    public String getHost()
    {
        return host;
    }
    public void setMac(String mac)
    {
        this.mac = mac;
    }

    public String getMac()
    {
        return mac;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("src", getSrc())
                .append("dst", getDst())
                .append("spt", getSpt())
                .append("dpt", getDpt())
                .append("ts", getTs())
                .append("protocol", getProtocol())
                .append("action", getAction())
                .append("module", getModule())
                .append("host", getHost())
                .append("mac", getMac())
                .toString();
    }
}
