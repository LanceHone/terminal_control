package com.ruoyi.access.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 控制日志对象 access_ctl_logs
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
public class AccessCtlLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "类型")
    private String type;

    /** $column.columnComment */
    @Excel(name = "源IP")
    private String src;

    /** $column.columnComment */
    @Excel(name = "目标IP")
    private String dst;

    /** $column.columnComment */
    @Excel(name = "源端口")
    private String spt;

    /** $column.columnComment */
    @Excel(name = "目标端口")
    private String dpt;

    /** $column.columnComment */
    @Excel(name = "时间")
    private String ts;

    public AccessCtlLogs() {
    }

    public AccessCtlLogs(String timestamp, String status, String srcIP, String dstIP, String srcPort, String dstPort) {
        this.ts = timestamp;
        this.src = srcIP;
        this.dst = dstIP;
        this.spt = srcPort;
        this.dpt =dstPort;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
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
    public void setTs(String ts) 
    {
        this.ts = ts;
    }

    public String getTs() 
    {
        return ts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("src", getSrc())
            .append("dst", getDst())
            .append("spt", getSpt())
            .append("dpt", getDpt())
            .append("ts", getTs())
            .toString();
    }
}
