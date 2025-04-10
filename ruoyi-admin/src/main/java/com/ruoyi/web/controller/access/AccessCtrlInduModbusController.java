package com.ruoyi.web.controller.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.model.modbus.ModbusFrame;
import com.ruoyi.access.mapper.AccessCtrlInduModbusMapper;
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

import java.io.FileWriter;
import java.io.IOException;
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
    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping("/list")
    public TableDataInfo list(AccessPolicyInduModbus modbus) {
        startPage();
        List<AccessPolicyInduModbus> list = modbusService.selectModbusList(modbus);
        return getDataTable(list);
    }

    /**
     * 获取策略详细信息
     */
    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(modbusService.selectModbusById(id));
    }

    /**
     * 新增Modbus策略
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyInduModbus modbus) {

        int rows = modbusService.insertModbus(modbus);
        writeJsonFile();
        return toAjax(rows);
    }

    /**
     * 修改Modbus策略
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyInduModbus modbus) {
        int rows = modbusService.updateModbus(modbus);
        writeJsonFile();
        return toAjax(rows);
    }

    /**
     * 修改Modbus策略状态
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateModbusStatus(@RequestBody AccessPolicyInduModbus modbus) {
        modbus.setUpdateBy(SecurityUtils.getUsername());
        int rows = modbusService.updateModbusStatus(modbus);
        writeJsonFile();
        return toAjax(rows);
    }

    /**
     * 删除Modbus策略
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "Modbus访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int rows = modbusService.deleteModbusByIds(ids);
        writeJsonFile();
        return toAjax(rows);
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

    String filePath = "/var/access/mdbf.json";
    @Autowired ObjectMapper mapper;
    private void writeJsonFile() {
        //todo 检查文件存在
        // 查询所有的状态为"正常"的策略
        List<AccessPolicyInduModbus> policies = modbusService.getActivePolicies();
        //把policies转json存到/var/access/modbus.json
        try {
            String jsonString = mapper.writeValueAsString(policies);
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}