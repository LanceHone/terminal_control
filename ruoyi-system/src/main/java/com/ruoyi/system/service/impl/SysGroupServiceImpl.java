package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysGroup;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysGroupMapper;
import com.ruoyi.system.service.ISysGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysGroupServiceImpl implements ISysGroupService {

    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Override
    @DataScope(deptAlias = "d")
    public List<SysGroup> selectGroupList(SysGroup sysGroup) {
        // 手动分页
//        PageUtils.startPage();
        List<SysGroup> list = sysGroupMapper.selectGroupList(sysGroup);
//        List<SysGroup> list = baseMapper.selectGroupListByConditions(sysGroup.getCode(), sysGroup.getName());
        return list;
    }

    @Override
    public int addGroup(SysGroup sysGroup) {
        return sysGroupMapper.insertGroup(sysGroup);
    }

    @Override
    public int updateGroup(SysGroup sysGroup) {
        return sysGroupMapper.updateGroup(sysGroup);
    }

    @Override
    public int deleteGroup(Long[] groupIds) {
        int rows = 0;
        for (Long groupId : groupIds) {
            rows += sysGroupMapper.deleteGroup(groupId);
        }
        return rows;
    }

    @Override
    public List<SysGroup> selectGroupAll() {
        return SpringUtils.getAopProxy(this).selectGroupList(new SysGroup());
    }

    @Override
    public SysGroup selectById(Long groupId) {
        return sysGroupMapper.selectGroupById(groupId);
    }
}