package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessPolicyApplTelnet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Telnet访问控制 数据层
 */
public interface AccessCtrlApplTelnetMapper {

    /**
     * 查询TELNET访问控制列表
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return TELNET访问控制集合
     */
    public List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetList(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 根据ID查询TELNET访问控制信息
     *
     * @param id TELNET访问控制ID
     * @return TELNET访问控制信息
     */
    public AccessPolicyApplTelnet selectAccessCtrlApplTelnetById(Long id);

    /**
     * 新增TELNET访问控制
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return 结果
     */
    public int insertAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 修改TELNET访问控制
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return 结果
     */
    public int updateAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet);

    /**
     * 删除TELNET访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccessCtrlApplTelnetByIds(Long[] ids);

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    public AccessPolicyApplTelnet checkUnique(@Param("serverName") String serverName, @Param("directory") String directory);

    /**
     * 根据状态查询TELNET访问控制列表
     *
     * @param status 状态
     * @return TELNET访问控制集合
     */
    public List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetListByStatus(@Param("status") String status);
}