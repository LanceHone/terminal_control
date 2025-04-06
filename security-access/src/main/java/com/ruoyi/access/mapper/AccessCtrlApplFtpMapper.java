package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessPolicyApplFtp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FTP访问控制 数据层
 */
public interface AccessCtrlApplFtpMapper {

    /**
     * 查询FTP访问控制列表
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return FTP访问控制集合
     */
    public List<AccessPolicyApplFtp> selectAccessCtrlApplFtpList(AccessPolicyApplFtp accessCtrlApplFtp);

    /**
     * 根据ID查询FTP访问控制信息
     *
     * @param id FTP访问控制ID
     * @return FTP访问控制信息
     */
    public AccessPolicyApplFtp selectAccessCtrlApplFtpById(Long id);

    /**
     * 新增FTP访问控制
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return 结果
     */
    public int insertAccessCtrlApplFtp(AccessPolicyApplFtp accessCtrlApplFtp);

    /**
     * 修改FTP访问控制
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return 结果
     */
    public int updateAccessCtrlApplFtp(AccessPolicyApplFtp accessCtrlApplFtp);

    /**
     * 删除FTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccessCtrlApplFtpByIds(Long[] ids);

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    public AccessPolicyApplFtp checkUnique(@Param("serverName") String serverName, @Param("directory") String directory);

    /**
     * 根据状态查询FTP访问控制列表
     *
     * @param status 状态
     * @return FTP访问控制集合
     */
    public List<AccessPolicyApplFtp> selectAccessCtrlApplFtpListByStatus(@Param("status") String status);
}