//package com.ruoyi.access.handler;
//
//import com.ruoyi.access.domain.enums.OSType;
//import org.apache.commons.io.output.ByteArrayOutputStream;
//import org.springframework.stereotype.Component;
//import org.apache.commons.exec.CommandLine;
//import org.apache.commons.exec.DefaultExecutor;
//import org.apache.commons.exec.ExecuteException;
//import org.apache.commons.exec.PumpStreamHandler;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
///**
// * 跨平台防火墙处理器
// */
//@Component
//public class CrossPlatformFirewallHandler {
//
//    /**
//     * 判断防火墙是否已开启
//     * @param osType 操作系统类型
//     * @return 防火墙是否开启
//     */
//    private boolean isFirewallOn(OSType osType) {
//        String command = getCheckFirewallCommand();
//        String output = executeCommandAndGetOutput(command);
//        return parseFirewallStatus(output);
//    }
//
//    /**
//     * 打开防火墙
//     * @return 命令是否执行成功
//     */
//
//    public boolean openFirewall() throws IOException, InterruptedException {
//        String command = getOpenFirewallCommand();
//        return executeCommand(command);
//    }
//
//    /**
//     * 关闭防火墙
//     * @return 命令是否执行成功
//     */
//    public boolean closeFirewall() throws IOException, InterruptedException {
//        String command = getCloseFirewallCommand();
//        return executeCommand(command);
//    }
//
//    /**
//     * 执行命令
//     * @param command
//     * @return
//     */
//    public boolean executeCommand(String command) throws IOException, InterruptedException {
//        Process process = null;
//        BufferedReader reader = null;
//        BufferedReader errorReader = null;
//        try {
//            process = Runtime.getRuntime().exec(command);
//            process.waitFor();
//
//            // 输出命令执行结果
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 输出错误信息
//            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            while ((line = errorReader.readLine()) != null) {
//                System.err.println(line);
//            }
//
//            // 判断命令是否执行成功
//            return process.exitValue() == 0;
//        } catch (Exception e) {
//            throw e;
////            e.printStackTrace();
////            return false;
//        } finally {
//            // 关闭流
//            try {
//                if (reader != null) {
//                    reader.close();
//                }
//                if (errorReader != null) {
//                    errorReader.close();
//                }
//                if (process != null) {
//                    process.destroy();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 执行系统命令并获取输出
//     * @param command 要执行的命令
//     * @return 命令输出
//     */
//    private static String executeCommandAndGetOutput(String command) {
//        CommandLine cmdLine = CommandLine.parse(command);
//        DefaultExecutor executor = new DefaultExecutor();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
//        executor.setStreamHandler(streamHandler);
//
//        try {
//            int exitValue = executor.execute(cmdLine);
//            if (exitValue == 0) {
//                return outputStream.toString();
//            } else {
//                return "";
//            }
//        } catch (ExecuteException e) {
//            System.err.println("命令执行失败：" + e.getMessage());
//            return "";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    /**
//     * 获取打开防火墙的命令
//     * @return 打开防火墙的命令
//     */
//    private String getOpenFirewallCommand() {
//        OSType osType = OSType.fromName();
//        switch (osType) {
//            case WINDOWS:
//                return "netsh advfirewall set allprofiles state on";
//            case LINUX:
//                return "sudo ufw enable";
//            default:
//                return "";
//        }
//    }
//
//    /**
//     * 获取关闭防火墙的命令
//     * @return 关闭防火墙的命令
//     */
//    private String getCloseFirewallCommand() {
//        OSType osType = OSType.fromName();
//        switch (osType) {
//            case WINDOWS:
//                return "netsh advfirewall set allprofiles state off";
//            case LINUX:
//                return "sudo ufw disable";
//            default:
//                return "";
//        }
//    }
//
//    /**
//     * 获取检查防火墙状态的命令
//     * @return 检查防火墙状态的命令
//     */
//    private String getCheckFirewallCommand() {
//        OSType osType = OSType.fromName();
//        switch (osType) {
//            case WINDOWS:
//                return "netsh advfirewall show allprofiles";
//            case LINUX:
//                return "sudo ufw status";
//            default:
//                return "";
//        }
//    }
//
//    /**
//     * 解析防火墙状态输出，判断防火墙是否开启
//     * @param output 命令输出
//     * @return 防火墙是否开启
//     */
//    public boolean parseFirewallStatus(String output) {
//        OSType osType = OSType.fromName();
//        switch (osType) {
//            case WINDOWS:
//                return output.contains("State                                 ON");
//            case LINUX:
//                return output.contains("Status: active");
//            default:
//                return false;
//        }
//    }
//}
