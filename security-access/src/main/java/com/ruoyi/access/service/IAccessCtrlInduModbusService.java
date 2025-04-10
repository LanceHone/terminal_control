package com.ruoyi.access.service;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.service.superdefine.IAccessPolicyService;

import java.util.List;

/**
 * 工业Modbus服务层
 */
public interface IAccessCtrlInduModbusService extends IAccessPolicyService {
    List<AccessPolicyInduModbus> selectModbusList(AccessPolicyInduModbus modbus);

    AccessPolicyInduModbus selectModbusById(Long id);

    int insertModbus(AccessPolicyInduModbus modbus);

    int updateModbus(AccessPolicyInduModbus modbus);

    int deleteModbusByIds(Long[] ids);

    String checkDeviceRegisterUnique(AccessPolicyInduModbus modbus);

    int updateModbusStatus(AccessPolicyInduModbus modbus);

    /**
     * 匹配校验
     * @param frame
     * @return
     */
    boolean checkAccess(ModbusFrame frame);

    @Deprecated
    public Thread startServer();

    public String checkAccessRequest(AccessPolicyInduModbus modbus);

    List<AccessPolicyInduModbus> getActivePolicies();
}
