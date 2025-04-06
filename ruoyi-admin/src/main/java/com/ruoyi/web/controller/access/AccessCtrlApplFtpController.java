package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessPolicyApplFtp;
import com.ruoyi.access.service.IAccessCtrlApplFtpService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FPT访问控制 前端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/access/appl/ftp")
public class AccessCtrlApplFtpController extends BaseController {

    @Autowired
    private IAccessCtrlApplFtpService accessCtrlApplService;

    /**
     * 查询FPT访问控制列表
     */
    @PreAuthorize("@ss.hasPermi('access:appl:ftp:list')")
    @GetMapping("/list")
    public AjaxResult list(AccessPolicyApplFtp accessCtrlApplFtp) {
        List<AccessPolicyApplFtp> list = accessCtrlApplService.selectAccessCtrlApplFtpList(accessCtrlApplFtp);
        return success(list);
    }

    /**
     * 根据ID查询FPT访问控制信息
     */
    @PreAuthorize("@ss.hasPermi('access:appl:ftp:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        AccessPolicyApplFtp accessCtrlApplFtp = accessCtrlApplService.selectAccessCtrlApplFtpById(id);
        return success(accessCtrlApplFtp);
    }

    /**
     * 新增FPT访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:ftp:add')")
    @Log(title = "FPT访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyApplFtp accessCtrlApplFtp) {
        if (StringUtils.isNotEmpty(accessCtrlApplFtp.getDirectory())) {
            accessCtrlApplFtp.setDirectory(accessCtrlApplFtp.getDirectory().trim());
        }
        return toAjax(accessCtrlApplService.insertAccessCtrlApplFtp(accessCtrlApplFtp));
    }

    /**
     * 修改FPT访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:ftp:edit')")
    @Log(title = "FPT访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyApplFtp accessCtrlApplFtp) {
        if (StringUtils.isNotEmpty(accessCtrlApplFtp.getDirectory())) {
            accessCtrlApplFtp.setDirectory(accessCtrlApplFtp.getDirectory().trim());
        }
        return toAjax(accessCtrlApplService.updateAccessCtrlApplFtp(accessCtrlApplFtp));
    }

    /**
     * 删除FPT访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:ftp:remove')")
    @Log(title = "FPT访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accessCtrlApplService.deleteAccessCtrlApplFtpByIds(ids));
    }
}