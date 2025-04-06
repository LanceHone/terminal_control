//package com.ruoyi.access.interceptor;
//
//import com.ruoyi.access.service.IAccessCtrlNetworkTcpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.net.Socket;
//import org.jnetpcap.Pcap;
//
//@Component
//public class PolicyInterceptor {
//
//    @Autowired
//    private IAccessCtrlNetworkTcpService policyService;
//
//    public boolean preHandle(Socket socket) throws IOException {
//        // 获取源IP和端口
//        String srcIp = socket.getInetAddress().getHostAddress();
//        int srcPort = socket.getPort();
//
//        // 获取目的IP和端口
//        String dstIp = socket.getLocalAddress().getHostAddress();
//        int dstPort = socket.getLocalPort();
//
//        // 调用策略服务检查是否允许
//        boolean allowed = policyService.isAccessAllowed(srcIp, srcPort, dstIp, dstPort);
//
//        if (!allowed) {
//            // 如果不允许，发送拒绝消息
//            OutputStream outputStream = socket.getOutputStream();
//            PrintWriter writer = new PrintWriter(outputStream);
//            writer.println("Access denied by network policy");
//            writer.flush();
//            socket.close();
//            return false;
//        }
//
//        return true;
//    }
//}