package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccessCtrlInduModbusMapper {
    List<AccessPolicyInduModbus> selectList(AccessPolicyInduModbus modbus);

    AccessPolicyInduModbus selectById(Long id);

    int insert(AccessPolicyInduModbus modbus);

    int update(AccessPolicyInduModbus modbus);

    int updateModbusStatus(AccessPolicyInduModbus modbus);

    int deleteByIds(Long[] ids);

    AccessPolicyInduModbus checkDeviceRegisterUnique(
            @Param("deviceId") String deviceId,
            @Param("registerAddress") String registerAddress
    );

    List<AccessPolicyInduModbus> findAllActive();

    AccessPolicyInduModbus checkAccess(@Param("deviceId") String deviceId, @Param("functionCode") String functionCode, @Param("rw") String rw);
}
