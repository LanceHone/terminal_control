package com.ruoyi.test.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MockClient {

    public void sendRequest(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            System.out.println("Connected to server: " + socket.getInetAddress().getHostAddress());

            // 这里可以添加发送请求的逻辑
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MockClient mockClient = new MockClient();
        mockClient.sendRequest("localhost", 53);
    }
}