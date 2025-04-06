package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessPolicyApplHttp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * HTTP访问控制 数据层
 */
public interface AccessCtrlApplHttpMapper {

    /**
     * 查询HTTP访问控制列表
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return HTTP访问控制集合
     */
    public List<AccessPolicyApplHttp> selectAccessCtrlApplHttpList(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 根据ID查询HTTP访问控制信息
     *
     * @param id HTTP访问控制ID
     * @return HTTP访问控制信息
     */
    public AccessPolicyApplHttp selectAccessCtrlApplHttpById(Long id);

    /**
     * 新增HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    public int insertAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 修改HTTP访问控制
     *
     * @param accessCtrlApplHttp HTTP访问控制信息
     * @return 结果
     */
    public int updateAccessCtrlApplHttp(AccessPolicyApplHttp accessCtrlApplHttp);

    /**
     * 删除HTTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccessCtrlApplHttpByIds(Long[] ids);

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    public AccessPolicyApplHttp checkUnique(@Param("serverName") String serverName, @Param("directory") String directory);

    /**
     * 根据状态查询HTTP访问控制列表
     *
     * @param status 状态
     * @return HTTP访问控制集合
     */
    public List<AccessPolicyApplHttp> selectAccessCtrlApplHttpListByStatus(@Param("status") String status);
}