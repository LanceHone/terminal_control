package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessMdbLogs;

import java.time.LocalDateTime;
import java.util.List;

/**
 * modbus控制日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-15
 */
public interface AccessMdbLogsMapper 
{
    /**
     * 查询modbus控制日志
     * 
     * @param id modbus控制日志主键
     * @return modbus控制日志
     */
    public AccessMdbLogs selectAccessMdbLogsById(Long id);

    /**
     * 查询modbus控制日志列表
     * 
     * @param accessMdbLogs modbus控制日志
     * @return modbus控制日志集合
     */
    public List<AccessMdbLogs> selectAccessMdbLogsList(AccessMdbLogs accessMdbLogs);

    /**
     * 新增modbus控制日志
     * 
     * @param accessMdbLogs modbus控制日志
     * @return 结果
     */
    public int insertAccessMdbLogs(AccessMdbLogs accessMdbLogs);

    /**
     * 修改modbus控制日志
     * 
     * @param accessMdbLogs modbus控制日志
     * @return 结果
     */
    public int updateAccessMdbLogs(AccessMdbLogs accessMdbLogs);

    /**
     * 删除modbus控制日志
     * 
     * @param id modbus控制日志主键
     * @return 结果
     */
    public int deleteAccessMdbLogsById(Long id);

    /**
     * 批量删除modbus控制日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccessMdbLogsByIds(Long[] ids);

    public void clear();

    public Integer countSize();

    void deleteBefore(LocalDateTime localDateTime);
}
