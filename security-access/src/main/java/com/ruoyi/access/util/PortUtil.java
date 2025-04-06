package com.ruoyi.access.util;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 端口工具类
 */
public class PortUtil {
    /**
     * 判断端口是否被占用
     * @param port
     * @return
     */
    public static boolean isPortInUse(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            //TODO
            return true;
        }
    }
}
