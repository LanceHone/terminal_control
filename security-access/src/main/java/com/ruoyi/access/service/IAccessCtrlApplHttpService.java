package com.ruoyi.access.service;

import com.ruoyi.access.domain.AccessPolicyApplHttp;
import com.ruoyi.access.service.superdefine.IAccessApplPolicyService;

import java.util.List;

/**
 * HTTP访问控制 服务层
 *
 * @author ruoyi
 */
public interface IAccessCtrlApplHttpService extends IAccessApplPolicyService {

    /**
     * 查询HTTP访问控制列表
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return HTTP访问控制集合
     */
    List<AccessPolicyApplHttp> selectAccessCtrlApplHttpList(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 根据ID查询HTTP访问控制信息
     *
     * @param id HTTP访问控制ID
     * @return HTTP访问控制信息
     */
    AccessPolicyApplHttp selectAccessCtrlApplHttpById(Long id);

    /**
     * 新增HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    int insertAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 修改HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    int updateAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 删除HTTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccessCtrlApplHttpByIds(Long[] ids);

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    AccessPolicyApplHttp checkUnique(String serverName, String directory);

    /**
     * 根据状态查询HTTP访问控制列表
     *
     * @param status 状态
     * @return HTTP访问控制集合
     */
    List<AccessPolicyApplHttp> selectAccessCtrlApplHttpListByStatus(String status);
}