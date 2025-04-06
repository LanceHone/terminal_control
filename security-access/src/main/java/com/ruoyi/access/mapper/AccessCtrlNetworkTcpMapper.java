package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.AccessPolicyNetworkTcp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网络层访问控制 数据层
 */
public interface AccessCtrlNetworkTcpMapper {

    /**
     * 查询网络层访问控制列表
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 网络层访问控制集合
     */
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpList(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 根据ID查询网络层访问控制信息
     *
     * @param id 网络层访问控制ID
     * @return 网络层访问控制信息
     */
    public AccessPolicyNetworkTcp selectAccessCtrlNetworkTcpById(Long id);

    /**
     * 新增网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    public int insertAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 修改网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    public int updateAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp);

    /**
     * 修改网络层访问控制状态
     * @param modbus
     * @return
     */
    public int updateStatus(AccessPolicyInduModbus modbus);

    /**
     * 删除网络层访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccessCtrlNetworkTcpByIds(Long[] ids);

    /**
     * 校验源IP和目的IP是否唯一
     *
     * @param sourceIp 源IP
     * @param targetIp 目的IP
     * @return 结果
     */
    public AccessPolicyNetworkTcp checkIpUnique(@Param("sourceIp") String sourceIp, @Param("targetIp") String targetIp);

    /**
     * 根据状态查询网络层访问控制列表
     *
     * @param status 状态
     * @return 网络层访问控制集合
     */
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpListByStatus(@Param("status") String status);

    /**
     * 判断是否允许访问
     *
     * @param srcIp
     * @param srcPort
     * @param dstIp
     * @param dstPort
     * @return
     */
    public boolean isAccessAllowed(@Param("srcIp") String srcIp, @Param("srcPort") int srcPort, @Param("dstIp") String dstIp, @Param("dstPort") int dstPort);
}