package com.ruoyi.test.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerTest {

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Mock server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // 处理客户端请求
                new Thread(() -> {
                    try {
                        // 这里可以添加处理逻辑
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Mock server stopped");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TcpServerTest mockServer = new TcpServerTest();
        mockServer.start(53);
    }
}