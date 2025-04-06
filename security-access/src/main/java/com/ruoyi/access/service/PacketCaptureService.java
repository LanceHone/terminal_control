package com.ruoyi.access.service;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

//@Service
public class PacketCaptureService implements CommandLineRunner {
    static {
        try {
            // 尝试加载 jnetpcap 本地库
//            System.load("src/lib/libjnetpcap.so");
//            System.load("C:\\Program Files\\Java\\jdk1.8.0_102\\bin\\jnetpcap.dll");
            System.loadLibrary("jnetpcap");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load jnetpcap library: " + e.getMessage());
            throw e;
        }
    }
//    @Autowired
    private IAccessCtrlNetworkTcpService policyService;

    @Override
    public void run(String... args) throws Exception {
        startPacketCapture();
    }

    public void startPacketCapture() {
        List<PcapIf> allDevList = new ArrayList<>();
        Pcap.findAllDevs(allDevList, new StringBuilder());
        for (int i = 0; i < allDevList.size(); i++) {
            System.out.println(allDevList.get(i));
        }

        for (PcapIf device : allDevList) {
            System.out.println("Starting packet capture on device: " + device.getName());
            try {
//            try (Pcap pcap = Pcap.openLive(device.getName(), 65536, Pcap.PromiscuousMode.PROMISCUOUS, 1000)) {
                Pcap pcap = Pcap.openLive(device.getName(), 65536, Pcap.MODE_PROMISCUOUS, 1000, new StringBuilder());
                pcap.loop(0, new PcapPacketHandler<String>() {
                    @Override
                    public void nextPacket(PcapPacket packet, String user) {
                        processPacket(packet);
                    }
                }, "JNetPcap");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processPacket(PcapPacket packet) {
        // Implement packet processing logic here
        // Extract source IP, source port, destination IP, destination port
        // Call policyService to check access policy
        // Decide to allow or block the request
        // 创建一个 JPacket 对象来处理数据包
        JPacket jPacket = packet;
//        JPacket jPacket = packet.getHeader(new JPacket(packet.size()));

        // 提取 IP 层信息
        Ip4 ip = new Ip4();
        if (!jPacket.hasHeader(ip)) {
            return; // 不是 IP 数据包
        }

        String srcIp = ip.source().toString();
        String dstIp = ip.destination().toString();

        // 提取传输层信息（TCP 或 UDP）
        boolean isTcp = jPacket.hasHeader(Tcp.ID);
        boolean isUdp = jPacket.hasHeader(Udp.ID);

        Integer srcPort = null;
        Integer dstPort = null;

        if (isTcp) {
            Tcp tcp = new Tcp();
            jPacket.getHeader(tcp);
            srcPort = tcp.source(); // 直接获取 int 类型的端口号
            dstPort = tcp.destination();
        }

        if (srcPort == null || dstPort == null) {
            return; // 不是 TCP 数据包
        }

        System.out.printf("Captured packet: %s:%d -> %s:%d%n", srcIp, srcPort, dstIp, dstPort);

        // 调用策略服务进行访问控制
        boolean allowed = policyService.isAccessAllowed(srcIp, srcPort, dstIp, dstPort);

        if (!allowed) {
            System.out.printf("Blocked request from %s:%d to %s:%d%n", srcIp, srcPort, dstIp, dstPort);
            // 在这里实现拒绝消息发送逻辑
            sendDenialResponse(srcIp, srcPort, dstIp, dstPort);
        }
    }

    private void sendDenialResponse(String srcIp, int srcPort, String dstIp, int dstPort) {
        // 实现拒绝响应逻辑
        // 这里可以使用原始套接字发送ICMP拒绝消息，或者简单记录日志
        try {
            // Convert IP addresses to InetAddress objects
            InetAddress sourceAddress = InetAddress.getByName(srcIp);
            InetAddress destinationAddress = InetAddress.getByName(dstIp);

            // Create a raw socket
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            // Set socket options
            socketChannel.socket().setReuseAddress(true);

            // Bind to a local port
            InetSocketAddress localAddress = new InetSocketAddress(0);
            socketChannel.socket().bind(localAddress);

            // Connect to the remote address
            InetSocketAddress remoteAddress = new InetSocketAddress(sourceAddress, srcPort);
            socketChannel.connect(remoteAddress);

            // Prepare the response packet
            ByteBuffer buffer = ByteBuffer.allocate(1500);
            buffer.order(ByteOrder.BIG_ENDIAN);

            // Craft the Ethernet frame
            // (This would typically be handled by the network stack, but shown for completeness)
            // ...

            // Craft the IP header
            buffer.put((byte) 0x45); // Version and IHL
            buffer.put((byte) 0x00); // Type of Service
            buffer.putShort((short) 0); // Total Length (will be filled in)
            buffer.putShort((short) 0); // Identification
            buffer.putShort((short) 0x4000); // Flags and Fragment Offset
            buffer.put((byte) 64); // TTL
            buffer.put((byte) 6); // Protocol (TCP)
            buffer.putShort((short) 0); // Header Checksum (will be filled in)
            buffer.putInt(sourceAddress.getAddress()[0] & 0xFF);
            buffer.putInt(sourceAddress.getAddress()[1] & 0xFF);
            buffer.putInt(sourceAddress.getAddress()[2] & 0xFF);
            buffer.putInt(sourceAddress.getAddress()[3] & 0xFF);
            buffer.putInt(destinationAddress.getAddress()[0] & 0xFF);
            buffer.putInt(destinationAddress.getAddress()[1] & 0xFF);
            buffer.putInt(destinationAddress.getAddress()[2] & 0xFF);
            buffer.putInt(destinationAddress.getAddress()[3] & 0xFF);

            // Craft the TCP header
            buffer.putShort((short) srcPort); // Source port
            buffer.putShort((short) dstPort); // Destination port
            buffer.putInt(0); // Sequence number
            buffer.putInt(0); // Acknowledgment number
            buffer.put((byte) 0x50); // Data offset and reserved
            buffer.put((byte) 0x14); // Control flags (RST + ACK)
            buffer.putShort((short) 0); // Window size
            buffer.putShort((short) 0); // Checksum (will be filled in)
            buffer.putShort((short) 0); // Urgent pointer

            // Calculate and set the checksum
            // (This is a simplified example and may need adjustment)
            short checksum = calculateChecksum(buffer.array(), buffer.position());
            buffer.putShort(buffer.position() - 2, checksum);

            // Send the packet
            socketChannel.write(buffer);

            // Close the socket
            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算校验和
     *
     * @param data
     * @param length
     * @return
     */
    private short calculateChecksum(byte[] data, int length) {
        // Implement checksum calculation
        // This is a simplified example
        int sum = 0;
        for (int i = 0; i < length; i += 2) {
            sum += (data[i] << 8) | (data[i + 1] & 0xFF);
            if (sum > 0xFFFF) {
                sum = (sum >> 16) + (sum & 0xFFFF);
            }
        }
        return (short) ~sum;
    }

//    public void startPacketCapture();
}
