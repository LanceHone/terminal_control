package com.ruoyi.access.domain;

/**
 * 应用Ftp访问控制配置
 */
public class AccessPolicyApplFtp extends ApplAccessPolicy {
    private Long id;
    private String serverName;
    private Integer port;
    private String directory;
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

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
