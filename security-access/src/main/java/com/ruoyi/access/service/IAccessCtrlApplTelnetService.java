package com.ruoyi.access.service;

import com.ruoyi.access.domain.AccessPolicyApplTelnet;
import com.ruoyi.access.service.superdefine.IAccessApplPolicyService;

import java.util.List;

/**
 * FTP访问控制 服务层
 *
 * @author ruoyi
 */
public interface IAccessCtrlApplTelnetService extends IAccessApplPolicyService {

    /**
     * 查询FTP访问控制列表
     *
     * @param accessCtrlApplTelnet FTP访问控制信息
     * @return FTP访问控制集合
     */
    List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetList(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 根据ID查询FTP访问控制信息
     *
     * @param id FTP访问控制ID
     * @return FTP访问控制信息
     */
    AccessPolicyApplTelnet selectAccessCtrlApplTelnetById(Long id);

    /**
     * 新增FTP访问控制
     *
     * @param accessCtrlApplTelnet FTP访问控制信息
     * @return 结果
     */
    int insertAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 修改FTP访问控制
     *
     * @param accessCtrlApplTelnet FTP访问控制信息
     * @return 结果
     */
    int updateAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 删除FTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccessCtrlApplTelnetByIds(Long[] ids);

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    AccessPolicyApplTelnet checkUnique(String serverName, String directory);

    /**
     * 根据状态查询FTP访问控制列表
     *
     * @param status 状态
     * @return FTP访问控制集合
     */
    List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetListByStatus(String status);
}