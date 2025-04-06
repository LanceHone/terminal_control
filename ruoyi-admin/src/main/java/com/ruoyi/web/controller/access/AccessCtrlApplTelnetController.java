package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessPolicyApplTelnet;
import com.ruoyi.access.service.IAccessCtrlApplTelnetService;
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
 * TELNET访问控制 前端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/access/appl/telnet")
public class AccessCtrlApplTelnetController extends BaseController {

    @Autowired
    private IAccessCtrlApplTelnetService accessCtrlApplService;

    /**
     * 查询TELNET访问控制列表
     */
    @PreAuthorize("@ss.hasPermi('access:appl:telnet:list')")
    @GetMapping("/list")
    public AjaxResult list(AccessPolicyApplTelnet accessCtrlApplTelnet) {
        List<AccessPolicyApplTelnet> list = accessCtrlApplService.selectAccessCtrlApplTelnetList(accessCtrlApplTelnet);
        return success(list);
    }

    /**
     * 根据ID查询TELNET访问控制信息
     */
    @PreAuthorize("@ss.hasPermi('access:appl:telnet:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        AccessPolicyApplTelnet accessCtrlApplTelnet = accessCtrlApplService.selectAccessCtrlApplTelnetById(id);
        return success(accessCtrlApplTelnet);
    }

    /**
     * 新增TELNET访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:telnet:add')")
    @Log(title = "TELNET访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyApplTelnet accessCtrlApplTelnet) {
//        if (StringUtils.isNotEmpty(accessCtrlApplTelnet.getDirectory())) {
//            accessCtrlApplTelnet.setDirectory(accessCtrlApplTelnet.getDirectory().trim());
//        }
        return toAjax(accessCtrlApplService.insertAccessCtrlApplTelnet(accessCtrlApplTelnet));
    }

    /**
     * 修改TELNET访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:telnet:edit')")
    @Log(title = "TELNET访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyApplTelnet accessCtrlApplTelnet) {
//        if (StringUtils.isNotEmpty(accessCtrlApplTelnet.getDirectory())) {
//            accessCtrlApplTelnet.setDirectory(accessCtrlApplTelnet.getDirectory().trim());
//        }
        return toAjax(accessCtrlApplService.updateAccessCtrlApplTelnet(accessCtrlApplTelnet));
    }

    /**
     * 删除TELNET访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:telnet:remove')")
    @Log(title = "TELNET访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accessCtrlApplService.deleteAccessCtrlApplTelnetByIds(ids));
    }
}