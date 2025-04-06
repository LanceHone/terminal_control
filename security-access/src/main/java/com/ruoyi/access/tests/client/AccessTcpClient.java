package com.ruoyi.access.tests.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 创建ModbusTCP客户端,实现连接和读写操作
 *
 * @Description: TODO
 * @author: KING
 * @see https://blog.csdn.net/weixin_29443363/article/details/142721448
 */
public class AccessTcpClient {
    private Socket clientSocket;
    private String ipAddress;
    private int port;

    public AccessTcpClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void connect() throws Exception {
        clientSocket = new Socket(ipAddress, port);
        // 检查连接状态可以使用clientSocket.isConnected();
    }

    public void disconnect() throws Exception {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }

    public void sendRequest(byte[] request) throws Exception {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(request);
    }

    public byte[] receiveResponse() throws Exception {
        InputStream inputStream = clientSocket.getInputStream();
        // 假定响应是1024字节，实际使用时可能需要根据实际情况进行调整
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        return bytesRead > 0 ? buffer : null;
    }
}
