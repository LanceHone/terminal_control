package com.ruoyi.access.service.impl;

import java.util.List;

import com.ruoyi.access.domain.AccessMdbLogs;
import com.ruoyi.access.mapper.AccessMdbLogsMapper;
import com.ruoyi.access.service.IAccessMdbLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * modbus控制日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
@Service
public class AccessMdbLogsServiceImpl implements IAccessMdbLogsService
{
    @Autowired
    private AccessMdbLogsMapper accessMdbLogsMapper;

    /**
     * 查询modbus控制日志
     * 
     * @param id modbus控制日志主键
     * @return modbus控制日志
     */
    @Override
    public AccessMdbLogs selectAccessMdbLogsById(Long id)
    {
        return accessMdbLogsMapper.selectAccessMdbLogsById(id);
    }

    /**
     * 查询modbus控制日志列表
     * 
     * @param accessMdbLogs modbus控制日志
     * @return modbus控制日志
     */
    @Override
    public List<AccessMdbLogs> selectAccessMdbLogsList(AccessMdbLogs accessMdbLogs)
    {
        return accessMdbLogsMapper.selectAccessMdbLogsList(accessMdbLogs);
    }

    /**
     * 新增modbus控制日志
     * 
     * @param accessMdbLogs modbus控制日志
     * @return 结果
     */
    @Override
    public int insertAccessMdbLogs(AccessMdbLogs accessMdbLogs)
    {
        return accessMdbLogsMapper.insertAccessMdbLogs(accessMdbLogs);
    }

    /**
     * 修改modbus控制日志
     * 
     * @param accessMdbLogs modbus控制日志
     * @return 结果
     */
    @Override
    public int updateAccessMdbLogs(AccessMdbLogs accessMdbLogs)
    {
        return accessMdbLogsMapper.updateAccessMdbLogs(accessMdbLogs);
    }

    /**
     * 批量删除modbus控制日志
     * 
     * @param ids 需要删除的modbus控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessMdbLogsByIds(Long[] ids)
    {
        return accessMdbLogsMapper.deleteAccessMdbLogsByIds(ids);
    }

    /**
     * 删除modbus控制日志信息
     * 
     * @param id modbus控制日志主键
     * @return 结果
     */
    @Override
    public int deleteAccessMdbLogsById(Long id)
    {
        return accessMdbLogsMapper.deleteAccessMdbLogsById(id);
    }
}
