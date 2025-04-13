package com.ruoyi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mapper.AccessCtlLogsMapper;
import com.ruoyi.domain.AccessCtlLogs;
import com.ruoyi.service.IAccessCtlLogsService;

/**
 * 控制日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
@Service
public class AccessCtlLogsServiceImpl implements IAccessCtlLogsService 
{
    @Autowired
    private AccessCtlLogsMapper accessCtlLogsMapper;

    /**
     * 查询控制日志
     * 
     * @param id 控制日志主键
     * @return 控制日志
     */
    @Override
    public AccessCtlLogs selectAccessCtlLogsById(Long id)
    {
        return accessCtlLogsMapper.selectAccessCtlLogsById(id);
    }

    /**
     * 查询控制日志列表
     * 
     * @param accessCtlLogs 控制日志
     * @return 控制日志
     */
    @Override
    public List<AccessCtlLogs> selectAccessCtlLogsList(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.selectAccessCtlLogsList(accessCtlLogs);
    }

    /**
     * 新增控制日志
     * 
     * @param accessCtlLogs 控制日志
     * @return 结果
     */
    @Override
    public int insertAccessCtlLogs(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.insertAccessCtlLogs(accessCtlLogs);
    }

    /**
     * 修改控制日志
     * 
     * @param accessCtlLogs 控制日志
     * @return 结果
     */
    @Override
    public int updateAccessCtlLogs(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.updateAccessCtlLogs(accessCtlLogs);
    }

    /**
     * 批量删除控制日志
     * 
     * @param ids 需要删除的控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessCtlLogsByIds(Long[] ids)
    {
        return accessCtlLogsMapper.deleteAccessCtlLogsByIds(ids);
    }

    /**
     * 删除控制日志信息
     * 
     * @param id 控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessCtlLogsById(Long id)
    {
        return accessCtlLogsMapper.deleteAccessCtlLogsById(id);
    }
}
