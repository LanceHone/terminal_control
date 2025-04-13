package com.ruoyi.mapper;

import java.util.List;
import com.ruoyi.domain.AccessCtlLogs;

/**
 * 控制日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
public interface AccessCtlLogsMapper 
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
     * 删除控制日志
     * 
     * @param id 控制日志主键
     * @return 结果
     */
    public int deleteAccessCtlLogsById(Long id);

    /**
     * 批量删除控制日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccessCtlLogsByIds(Long[] ids);
}
