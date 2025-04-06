//package com.ruoyi.access.listener;
//
//import com.sun.tools.jdi.Packet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.util.Enumeration;
//import java.util.List;
//
//@Component
//public class NetworkTrafficListener implements Runnable {
//
//    @Autowired
//    private AccessPolicyService accessPolicyService;
//
//    private boolean running = true;
//
//    public void startListening() {
//        new Thread(this).start();
//    }
//
//    @Override
//    public void run() {
//        try {
//            // 获取所有网络接口
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements() && running) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//                // 跳过回环接口
//                if (networkInterface.isLoopback()) continue;
//
//                // 获取网卡的 IP 地址
//                List<InetAddress> addresses = getInetAddresses(networkInterface);
//                for (InetAddress ip : addresses) {
//                    if (ip.isSiteLocalAddress()) {
//                        // 开始捕获该网卡的流量
//                        startPacketCapture(networkInterface, ip.getHostAddress());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<InetAddress> getInetAddresses(NetworkInterface networkInterface) {
//        // 实现获取网卡地址的逻辑
//        return List.of(); // 简化示例
//    }
//
//    private void startPacketCapture(NetworkInterface networkInterface, String ipAddress) {
//        // 使用 Jpcap 进行数据包捕获
//        JpcapCaptor captor = JpcapCaptor.openDevice(networkInterface, 65536, true, 20);
//        captor.loopPacket(-1, (Packet packet) -> {
//            processPacket(packet, ipAddress);
//        });
//    }
//
//    private void processPacket(Packet packet, String localIpAddress) {
//        // 处理 TCP 包
//        if (packet instanceof TcpPacket) {
//            TcpPacket tcpPacket = (TcpPacket) packet;
//            handleTransportPacket(tcpPacket, localIpAddress);
//        }
//        // 处理 UDP 包
//        else if (packet instanceof UdpPacket) {
//            UdpPacket udpPacket = (UdpPacket) packet;
//            handleTransportPacket(udpPacket, localIpAddress);
//        }
//    }
//
//    private void handleTransportPacket(TransportPacket packet, String localIpAddress) {
//        // 获取源 IP、源端口、目的 IP、目的端口
//        String srcIP = packet.src_ip.getHostAddress();
//        int srcPort = packet.src_port;
//        String dstIP = packet.dst_ip.getHostAddress();
//        int dstPort = packet.dst_port;
//
//        // 检查是否是发往本机的请求
//        if (dstIP.equals(localIpAddress)) {
//            // 根据策略决定是否允许
//            boolean allowed = accessPolicyService.checkAccessPolicy(srcIP, srcPort, dstIP, dstPort);
//            if (!allowed) {
//                sendDenialResponse(srcIP, srcPort, dstIP, dstPort, packet);
//            }
//        }
//    }
//
//    private void sendDenialResponse(String srcIP, int srcPort,
//                                    String dstIP, int dstPort,
//                                    TransportPacket originalPacket) {
//        // 实现发送拒绝响应的逻辑
//    }
//
//    public void stopListening() {
//        running = false;
//    }
//}