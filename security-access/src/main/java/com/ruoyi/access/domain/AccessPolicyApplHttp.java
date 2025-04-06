package com.ruoyi.access.domain;

/**
 * 应用HTTP访问控制配置
 */
public class AccessPolicyApplHttp extends ApplAccessPolicy {
    private Long id;
    private String serverName;
    private Integer port;
//    private String directory;
    private char status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
