package com.ruoyi.access.service;

import org.springframework.boot.CommandLineRunner;

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

    }

    private void processPacket() {
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
