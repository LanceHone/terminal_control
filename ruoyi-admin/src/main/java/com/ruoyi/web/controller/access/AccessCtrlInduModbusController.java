package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.service.IAccessCtrlInduModbusService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工业协议Modbus策略Controller
 */
@RestController
@RequestMapping("/access/modbus")
public class AccessCtrlInduModbusController extends BaseController {

    @Autowired
    private IAccessCtrlInduModbusService modbusService;

    /**
     * 查询Modbus策略列表
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccessPolicyInduModbus modbus) {
        startPage();
        List<AccessPolicyInduModbus> list = modbusService.selectModbusList(modbus);
        return getDataTable(list);
    }

    /**
     * 获取策略详细信息
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(modbusService.selectModbusById(id));
    }

    /**
     * 新增Modbus策略
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:add')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyInduModbus modbus) {
        return toAjax(modbusService.insertModbus(modbus));
    }

    /**
     * 修改Modbus策略
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:edit')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyInduModbus modbus) {
        return toAjax(modbusService.updateModbus(modbus));
    }

    /**
     * 修改Modbus策略状态
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:edit')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateModbusStatus(@RequestBody AccessPolicyInduModbus modbus) {
        modbus.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(modbusService.updateModbusStatus(modbus));
    }

    /**
     * 删除Modbus策略
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:remove')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(modbusService.deleteModbusByIds(ids));
    }

    /**
     * 校验设备与寄存器唯一性
     */
    @PostMapping("/checkUnique")
    public AjaxResult checkUnique(@RequestBody AccessPolicyInduModbus modbus) {
        return success(modbusService.checkDeviceRegisterUnique(modbus));
    }
    /**
     * 校验设备与寄存器唯一性
     */
    @PostMapping("/checkAccess")
    public AjaxResult checkAccess(@RequestBody AccessPolicyInduModbus modbus) {

        String result = modbusService.checkAccessRequest(modbus);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return success(resultMap);
    }
}