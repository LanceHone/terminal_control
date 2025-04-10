package com.ruoyi.access.domain;

import com.ruoyi.common.annotation.Excel;

/**
 * TCP网络控制对象
 */
public class AccessPolicyNetworkTcp extends AccessPolicy {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String sourceIp = "0.0.0.0/0";
    private Integer sourcePort = 0;
    private String targetIp = "0.0.0.0/0";
    private Integer targetPort = 0;
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status = "1";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
