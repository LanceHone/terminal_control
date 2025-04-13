package com.ruoyi.access.service;

import com.ruoyi.access.domain.AccessCtlLogs;

import java.util.List;

/**
 * 控制日志Service接口
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
public interface IAccessCtlLogsService 
{
    /**
     * 查询控制日志
     * 
     * @param id 控制日志主键
     * @return 控制日志
     */
    public AccessCtlLogs selectAccessCtlLogsById(Long id);

    /**
     * 查询控制日志列表
     * 
     * @param accessCtlLogs 控制日志
     * @return 控制日志集合
     */
    public List<AccessCtlLogs> selectAccessCtlLogsList(AccessCtlLogs accessCtlLogs);

    /**
     * 新增控制日志
     * 
     * @param accessCtlLogs 控制日志
     * @return 结果
     */
    public int insertAccessCtlLogs(AccessCtlLogs accessCtlLogs);

    /**
     * 修改控制日志
     * 
     * @param accessCtlLogs 控制日志
     * @return 结果
     */
    public int updateAccessCtlLogs(AccessCtlLogs accessCtlLogs);

    /**
     * 批量删除控制日志
     * 
     * @param ids 需要删除的控制日志主键集合
     * @return 结果
     */
    public int deleteAccessCtlLogsByIds(Long[] ids);

    /**
     * 删除控制日志信息
     * 
     * @param id 控制日志主键
     * @return 结果
     */
    public int deleteAccessCtlLogsById(Long id);
}
