package com.ruoyi.access.service;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.AccessPolicyNetworkTcp;
import com.ruoyi.access.service.superdefine.IAccessPolicyService;

import java.util.List;

/**
 * 网络层访问控制 服务层
 *
 * @author ruoyi
 */
public interface IAccessCtrlNetworkTcpService extends IAccessPolicyService {

    /**
     * 查询网络层访问控制列表
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 网络层访问控制集合
     */
    List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpList(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 根据ID查询网络层访问控制信息
     *
     * @param id 网络层访问控制ID
     * @return 网络层访问控制信息
     */
    AccessPolicyNetworkTcp selectAccessCtrlNetworkTcpById(Long id);

    /**
     * 新增网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    int insertAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 修改网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    int updateAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 修改网络层访问控制状态
     *
     * @param modbus 网络层访问控制信息
     * @return 结果
     */
    int updateStatus(AccessPolicyNetworkTcp modbus);

    /**
     * 删除网络层访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccessCtrlNetworkTcpByIds(Long[] ids);

    /**
     * 校验源IP和目的IP是否唯一
     *
     * @param sourceIp 源IP
     * @param targetIp 目的IP
     * @return 结果
     */
    AccessPolicyNetworkTcp checkIpUnique(String sourceIp, String targetIp);

    /**
     * 根据状态查询网络层访问控制列表
     *
     * @param status 状态
     * @return 网络层访问控制集合
     */
    List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpListByStatus(String status);

    /**
     * 判断是否允许访问
     * @param srcIp
     * @param srcPort
     * @param dstIp
     * @param dstPort
     * @return
     */
    boolean isAccessAllowed(String srcIp, int srcPort, String dstIp, int dstPort);

    boolean checkAccess(Object msg);

    void updateWhiteList(AccessPolicyNetworkTcp accessCtrlNetworkTcp, String add);
}