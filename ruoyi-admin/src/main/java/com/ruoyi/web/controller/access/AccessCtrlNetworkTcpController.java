package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessPolicyInduModbus;
import com.ruoyi.access.domain.AccessPolicyNetworkTcp;
import com.ruoyi.access.service.IAccessCtrlNetworkTcpService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网络层访问控制 前端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/access/tcp")
@Validated
public class AccessCtrlNetworkTcpController extends BaseController {

    @Autowired
    private IAccessCtrlNetworkTcpService accessCtrlNetworkTcpService;

    /**
     * 获取网络层访问控制列表
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:list')")
    @GetMapping("/list")
    public AjaxResult list(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        List<AccessPolicyNetworkTcp> list = accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpList(accessCtrlNetworkTcp);
        return success(list);
    }

    /**
     * 根据ID获取网络层访问控制详细信息
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpById(id));
    }

    /**
     * 新增网络层访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:add')")
    @Log(title = "网络层访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        if (StringUtils.isNotNull(accessCtrlNetworkTcpService.checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
            return error("新增网络层访问控制失败，源IP和目的IP已存在");
        }
        accessCtrlNetworkTcp.setCreateBy(getUsername());
        return toAjax(accessCtrlNetworkTcpService.insertAccessCtrlNetworkTcp(accessCtrlNetworkTcp));
    }

    /**
     * 修改网络层访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:edit')")
    @Log(title = "网络层访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        if (StringUtils.isNotNull(accessCtrlNetworkTcpService.checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
            return error("修改网络层访问控制失败，源IP和目的IP已存在");
        }
        accessCtrlNetworkTcp.setUpdateBy(getUsername());
        return toAjax(accessCtrlNetworkTcpService.updateAccessCtrlNetworkTcp(accessCtrlNetworkTcp));
    }

    /**
     * 修改策略状态
     */
    @PreAuthorize("@ss.hasPermi('access:modbus:edit')")
    @Log(title = "修改策略状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateStatus(@RequestBody AccessPolicyInduModbus modbus) {
        modbus.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(accessCtrlNetworkTcpService.updateStatus(modbus));
    }

    /**
     * 删除网络层访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:remove')")
    @Log(title = "网络层访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(accessCtrlNetworkTcpService.deleteAccessCtrlNetworkTcpByIds(new Long[]{id}));
    }

    /**
     * 批量删除网络层访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:tcp:remove')")
    @Log(title = "网络层访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult removeBatch(@RequestBody Long[] ids) {
        return toAjax(accessCtrlNetworkTcpService.deleteAccessCtrlNetworkTcpByIds(ids));
    }
}