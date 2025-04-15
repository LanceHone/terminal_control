package com.ruoyi.access.service.impl;

import java.util.List;

import com.ruoyi.access.domain.AccessCtlLogs;
import com.ruoyi.access.mapper.AccessCtlLogsMapper;
import com.ruoyi.access.service.IAccessCtlLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 访问控制日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-15
 */
@Service
public class AccessCtlLogsServiceImpl implements IAccessCtlLogsService
{
    @Autowired
    private AccessCtlLogsMapper accessCtlLogsMapper;

    /**
     * 查询访问控制日志
     * 
     * @param id 访问控制日志主键
     * @return 访问控制日志
     */
    @Override
    public AccessCtlLogs selectAccessCtlLogsById(Long id)
    {
        return accessCtlLogsMapper.selectAccessCtlLogsById(id);
    }

    /**
     * 查询访问控制日志列表
     * 
     * @param accessCtlLogs 访问控制日志
     * @return 访问控制日志
     */
    @Override
    public List<AccessCtlLogs> selectAccessCtlLogsList(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.selectAccessCtlLogsList(accessCtlLogs);
    }

    /**
     * 新增访问控制日志
     * 
     * @param accessCtlLogs 访问控制日志
     * @return 结果
     */
    @Override
    public int insertAccessCtlLogs(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.insertAccessCtlLogs(accessCtlLogs);
    }

    /**
     * 修改访问控制日志
     * 
     * @param accessCtlLogs 访问控制日志
     * @return 结果
     */
    @Override
    public int updateAccessCtlLogs(AccessCtlLogs accessCtlLogs)
    {
        return accessCtlLogsMapper.updateAccessCtlLogs(accessCtlLogs);
    }

    /**
     * 批量删除访问控制日志
     * 
     * @param ids 需要删除的访问控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessCtlLogsByIds(Long[] ids)
    {
        return accessCtlLogsMapper.deleteAccessCtlLogsByIds(ids);
    }

    /**
     * 删除访问控制日志信息
     * 
     * @param id 访问控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessCtlLogsById(Long id)
    {
        return accessCtlLogsMapper.deleteAccessCtlLogsById(id);
    }
}
