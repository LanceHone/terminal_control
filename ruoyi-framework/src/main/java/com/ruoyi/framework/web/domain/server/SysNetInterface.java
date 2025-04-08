package com.ruoyi.framework.web.domain.server;

public class SysNetInterface {
    private String name;
    private String displayName;
    private String bytesRecv;
    private String bytesSent;
    private String packetsRecv;
    private String packetsSent;
    private String upSpeed;
    private String downSpeed;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setBytesRecv(String bytesRecv) {
        this.bytesRecv = bytesRecv;
    }

    public String getBytesRecv() {
        return bytesRecv;
    }

    public void setBytesSent(String bytesSent) {
        this.bytesSent = bytesSent;
    }

    public String getBytesSent() {
        return bytesSent;
    }

    public void setPacketsRecv(String packetsRecv) {
        this.packetsRecv = packetsRecv;
    }

    public String getPacketsRecv() {
        return packetsRecv;
    }

    public void setPacketsSent(String packetsSent) {
        this.packetsSent = packetsSent;
    }

    public String getPacketsSent() {
        return packetsSent;
    }

    // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd HH:mm:ss z", Locale.US);

    public String getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(String upSpeed) {
        this.upSpeed = upSpeed;
    }

    public String getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(String downSpeed) {
        this.downSpeed = downSpeed;
    }
}
