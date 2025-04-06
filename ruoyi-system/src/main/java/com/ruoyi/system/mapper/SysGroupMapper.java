package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//@Mapper
public interface SysGroupMapper {

    /**
     * 查询组别列表
     */
    List<SysGroup> selectGroupList(SysGroup sysGroup);

    SysGroup selectGroupById(Long id);

    int insertGroup(SysGroup sysGroup);

    int updateGroup(SysGroup sysGroup);

    int deleteGroup(@Param("id") Long id);
}