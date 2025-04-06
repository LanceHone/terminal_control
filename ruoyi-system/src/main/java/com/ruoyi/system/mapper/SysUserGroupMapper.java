package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserGroupMapper {
    void deleteUserGroupByUserId(@Param("userId") Long userId);

    int countUserGroupByGroupId(@Param("groupId") Long groupId);

    void deleteUserGroup(@Param("userIdList") Long[] userIds);

    void batchUserGroup(List<SysUserGroup> userGroupList);

    void deleteUserGroupInfo(SysUserGroup userGroup);

    void deleteUserGroupInfos(@Param("groupId") Long groupId, @Param("userIds") List<Long> userIds);
}
