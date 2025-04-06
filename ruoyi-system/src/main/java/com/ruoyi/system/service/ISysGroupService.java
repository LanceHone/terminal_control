package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysGroup;

import java.util.List;

public interface ISysGroupService {

    /**
     * 查询组别列表
     * @param sysGroup 查询条件
     * @param pageQuery 分页查询
     * @return 组别列表
     */
    List<SysGroup> selectGroupList(SysGroup sysGroup);

    /**
     * 新增组别
     * @param sysGroup 组别信息
     * @return 结果
     */
    int addGroup(SysGroup sysGroup);

    /**
     * 修改组别
     * @param sysGroup 组别信息
     * @return 结果
     */
    int updateGroup(SysGroup sysGroup);

    /**
     * 批量删除组别
     * @param groupIds 需要删除的组别ID
     * @return 结果
     */
    int deleteGroup(Long[] groupIds);

    List<SysGroup> selectGroupAll();

    SysGroup selectById(Long groupId);
}