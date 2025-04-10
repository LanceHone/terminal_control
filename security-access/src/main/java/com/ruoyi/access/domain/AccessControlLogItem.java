package com.ruoyi.access.domain;

public class AccessControlLogItem {
    private Long id;
    private String timestamp;
    private String status;
    private String src_ip;
    private String dst_ip;
    private int src_port;
    private int dst_port;

    public AccessControlLogItem(String timestamp, String status, String src_ip, String dst_ip, int src_port, int dst_port) {
        this.timestamp = timestamp;
        this.status = status;
        this.src_ip = src_ip;
        this.dst_ip = dst_ip;
        this.src_port = src_port;
        this.dst_port = dst_port;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s SRC=%s:%d -> DST=%s:%d",
                timestamp, status, src_ip, src_port, dst_ip, dst_port);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String getDst_ip() {
        return dst_ip;
    }

    public void setDst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public int getSrc_port() {
        return src_port;
    }

    public void setSrc_port(int src_port) {
        this.src_port = src_port;
    }

    public int getDst_port() {
        return dst_port;
    }

    public void setDst_port(int dst_port) {
        this.dst_port = dst_port;
    }
}

