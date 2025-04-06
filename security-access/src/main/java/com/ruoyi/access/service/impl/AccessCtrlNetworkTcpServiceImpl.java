package com.ruoyi.access.service.impl;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.AccessPolicyNetworkTcp;
import com.ruoyi.access.mapper.AccessCtrlNetworkTcpMapper;
import com.ruoyi.access.service.IAccessCtrlNetworkTcpService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网络层访问控制 服务实现
 *
 * @author ruoyi
 */
@Service
public class AccessCtrlNetworkTcpServiceImpl implements IAccessCtrlNetworkTcpService {

    @Autowired
    private AccessCtrlNetworkTcpMapper accessCtrlNetworkTcpMapper;

    /**
     * 查询网络层访问控制列表
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 网络层访问控制集合
     */
    @Override
    @DataScope
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpList(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpList(accessCtrlNetworkTcp);
    }

    /**
     * 根据ID查询网络层访问控制信息
     *
     * @param id 网络层访问控制ID
     * @return 网络层访问控制信息
     */
    @Override
    public AccessPolicyNetworkTcp selectAccessCtrlNetworkTcpById(Long id) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpById(id);
    }

    /**
     * 新增网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    @Override
    public int insertAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        // 校验唯一性
        if (StringUtils.isNotNull(checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
            throw new ServiceException("源IP和目的IP已存在，不能重复添加");
        }
        accessCtrlNetworkTcp.setCreateBy(SecurityUtils.getUsername());
        return accessCtrlNetworkTcpMapper.insertAccessCtrlNetworkTcp(accessCtrlNetworkTcp);
    }

    /**
     * 修改网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    @Override
    public int updateAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        // 校验唯一性
        if (StringUtils.isNotNull(checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
            throw new ServiceException("源IP和目的IP已存在，不能重复添加");
        }
        accessCtrlNetworkTcp.setUpdateBy(SecurityUtils.getUsername());
        return accessCtrlNetworkTcpMapper.updateAccessCtrlNetworkTcp(accessCtrlNetworkTcp);
    }

    @Override
    public int updateStatus(AccessPolicyInduModbus modbus) {
        return accessCtrlNetworkTcpMapper.updateStatus(modbus);
    }

    /**
     * 删除网络层访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccessCtrlNetworkTcpByIds(Long[] ids) {
        return accessCtrlNetworkTcpMapper.deleteAccessCtrlNetworkTcpByIds(ids);
    }

    /**
     * 校验源IP和目的IP是否唯一
     *
     * @param sourceIp 源IP
     * @param targetIp 目的IP
     * @return 结果
     */
    @Override
    public AccessPolicyNetworkTcp checkIpUnique(String sourceIp, String targetIp) {
        return accessCtrlNetworkTcpMapper.checkIpUnique(sourceIp, targetIp);
    }

    /**
     * 根据状态查询网络层访问控制列表
     *
     * @param status 状态
     * @return 网络层访问控制集合
     */
    @Override
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpListByStatus(String status) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpListByStatus(status);
    }

    @Override
    public boolean isAccessAllowed(String srcIp, int srcPort, String dstIp, int dstPort) {
        return accessCtrlNetworkTcpMapper.isAccessAllowed(srcIp, srcPort, dstIp, dstPort);
    }

    @Override
    public boolean checkAccess(Object msg) {
        return false;
    }

    @Override
    public Boolean enablePolicy(Long policyId) {
        return null;
    }

    @Override
    public Boolean disablePolicy(Long policyId) {
        return null;
    }
}