package com.ruoyi.access.tests.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ModbusTcpServer {
    private ServerSocket serverSocket;
    private int port;

    public ModbusTcpServer(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void start() throws Exception {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    public void stop() throws Exception {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                // 读取请求数据和发送响应的代码应放在这里
                // 需要实现Modbus协议帧的解析和响应的构建
                byte[] request = new byte[256];
                int bytesRead = inputStream.read(request);
                byte[] actualRequest = Arrays.copyOf(request, bytesRead);

                System.out.println("Received request: " + Arrays.toString(actualRequest));

                // Process the request and generate a response
                byte[] response = processRequest(actualRequest);

                // Send the response back to the client
                outputStream.write(response);
                outputStream.flush();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private byte[] processRequest(byte[] request) {
            // For demonstration purposes, return a fixed response
            byte[] response = {
                    0x01,  // Transaction Identifier
                    0x00,  // Protocol Identifier
                    0x00, 0x05,  // Length
                    0x01,  // Unit Identifier
                    0x03,  // Function Code (0x03: Read Holding Registers)
                    0x02,  // Byte Count
                    0x00, 0x0A,  // Register Value 1 (0x000A)
                    0x00, 0x0B   // Register Value 2 (0x000B)
            };

            System.out.println("Sending response: " + Arrays.toString(response));
            return response;
        }
    }
}