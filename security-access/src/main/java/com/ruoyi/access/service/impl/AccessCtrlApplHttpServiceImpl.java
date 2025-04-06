package com.ruoyi.access.service.impl;

import com.ruoyi.access.domain.AccessPolicyApplHttp;
import com.ruoyi.access.mapper.AccessCtrlApplHttpMapper;
import com.ruoyi.access.service.IAccessCtrlApplHttpService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * HTTP访问控制 服务实现
 *
 * @author ruoyi
 */
@Service
public class AccessCtrlApplHttpServiceImpl implements IAccessCtrlApplHttpService {

    @Autowired
    private AccessCtrlApplHttpMapper accessCtrlApplHttpMapper;

    /**
     * 查询HTTP访问控制列表
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return HTTP访问控制集合
     */
    @Override
    @DataScope
    public List<AccessPolicyApplHttp> selectAccessCtrlApplHttpList(AccessPolicyApplHttp accessCtrlApplHttp) {
        return accessCtrlApplHttpMapper.selectAccessCtrlApplHttpList(accessCtrlApplHttp);
    }

    /**
     * 根据ID查询HTTP访问控制信息
     *
     * @param id HTTP访问控制ID
     * @return HTTP访问控制信息
     */
    @Override
    public AccessPolicyApplHttp selectAccessCtrlApplHttpById(Long id) {
        return accessCtrlApplHttpMapper.selectAccessCtrlApplHttpById(id);
    }

    /**
     * 新增HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    @Override
    public int insertAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp) {
        // 校验唯一性
//        if (StringUtils.isNotNull(checkUnique(accessCtrlApplHttp.getServerName(), accessCtrlApplHttp.getDirectory()))) {
//            throw new ServiceException("服务名称和目录已存在，不能重复添加");
//        }
        accessCtrlApplHttp.setCreateBy(SecurityUtils.getUsername());
        return accessCtrlApplHttpMapper.insertAccessCtrlApplHttp(accessCtrlApplHttp);
    }

    /**
     * 修改HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    @Override
    public int updateAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp) {
        // 校验唯一性
//        if (StringUtils.isNotNull(checkUnique(accessCtrlApplHttp.getServerName(), accessCtrlApplHttp.getDirectory()))) {
//            throw new ServiceException("服务名称和目录已存在，不能重复添加");
//        }
        accessCtrlApplHttp.setUpdateBy(SecurityUtils.getUsername());
        return accessCtrlApplHttpMapper.updateAccessCtrlApplHttp(accessCtrlApplHttp);
    }

    /**
     * 删除HTTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccessCtrlApplHttpByIds(Long[] ids) {
        return accessCtrlApplHttpMapper.deleteAccessCtrlApplHttpByIds(ids);
    }

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    @Override
    public AccessPolicyApplHttp checkUnique(String serverName, String directory) {
        return accessCtrlApplHttpMapper.checkUnique(serverName, directory);
    }

    /**
     * 根据状态查询HTTP访问控制列表
     *
     * @param status 状态
     * @return HTTP访问控制集合
     */
    @Override
    public List<AccessPolicyApplHttp> selectAccessCtrlApplHttpListByStatus(String status) {
        return accessCtrlApplHttpMapper.selectAccessCtrlApplHttpListByStatus(status);
    }

    @Override
    public Boolean enablePolicy(Long policyId) {
        return null;
    }

    @Override
    public Boolean disablePolicy(Long policyId) {
        return null;
    }

    /**
     * 检查是否允许访问
     * TODO
     * @return true 如果允许访问，false 否则
     */
    public boolean isAccessAllowed() {
        try {
            // 获取所有网卡
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 获取网卡的IP地址和MAC地址
                String macAddress = getMACAddress(networkInterface);
                List<String> ipAddresses = getIPAddresses(networkInterface);

                // 遍历所有IP地址进行检查
                for (String ipAddress : ipAddresses) {
                    if (isAllowed(ipAddress, macAddress)) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取网卡的MAC地址
     *
     * @param networkInterface 网卡接口
     * @return MAC地址
     */
    private String getMACAddress(NetworkInterface networkInterface) throws SocketException {
        byte[] macBytes = networkInterface.getHardwareAddress();
        if (macBytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : macBytes) {
            sb.append(String.format("%02X:", b));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 获取网卡的所有IPv4地址
     *
     * @param networkInterface 网卡接口
     * @return IPv4地址列表
     */
    private List<String> getIPAddresses(NetworkInterface networkInterface) {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        List<String> ipAddresses = new java.util.ArrayList<>();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                ipAddresses.add(inetAddress.getHostAddress());
            }
        }
        return ipAddresses;
    }

    /**
     * 定义访问控制规则
     *
     * @param ipAddress  IP地址
     * @param macAddress MAC地址
     * @return true 如果允许访问，false 否则
     */
    private boolean isAllowed(String ipAddress, String macAddress) {
        // 这里可以根据实际需求定义允许的IP地址和MAC地址
        // 例如，只允许特定IP地址访问
        if ("192.168.1.100".equals(ipAddress)) {
            return true;
        }
        // 或者只允许特定MAC地址访问
        if ("00:1A:2B:3C:4D:5E".equals(macAddress)) {
            return true;
        }
        return false;
    }
}