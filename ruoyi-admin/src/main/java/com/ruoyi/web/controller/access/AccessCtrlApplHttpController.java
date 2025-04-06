package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessPolicyApplHttp;
import com.ruoyi.access.service.IAccessCtrlApplHttpService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HTTP访问控制 前端控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/access/appl/http")
public class AccessCtrlApplHttpController extends BaseController {

    @Autowired
    private IAccessCtrlApplHttpService accessCtrlApplService;

    /**
     * 查询HTTP访问控制列表
     */
    @PreAuthorize("@ss.hasPermi('access:appl:http:list')")
    @GetMapping("/list")
    public AjaxResult list(AccessPolicyApplHttp accessCtrlApplHttp) {
        List<AccessPolicyApplHttp> list = accessCtrlApplService.selectAccessCtrlApplHttpList(accessCtrlApplHttp);
        return success(list);
    }

    /**
     * 根据ID查询HTTP访问控制信息
     */
    @PreAuthorize("@ss.hasPermi('access:appl:http:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        AccessPolicyApplHttp accessCtrlApplHttp = accessCtrlApplService.selectAccessCtrlApplHttpById(id);
        return success(accessCtrlApplHttp);
    }

    /**
     * 新增HTTP访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:http:add')")
    @Log(title = "HTTP访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyApplHttp accessCtrlApplHttp) {
//        if (StringUtils.isNotEmpty(accessCtrlApplHttp.getDirectory())) {
//            accessCtrlApplHttp.setDirectory(accessCtrlApplHttp.getDirectory().trim());
//        }
        return toAjax(accessCtrlApplService.insertAccessCtrlApplHttp(accessCtrlApplHttp));
    }

    /**
     * 修改HTTP访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:http:edit')")
    @Log(title = "HTTP访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyApplHttp accessCtrlApplHttp) {
//        if (StringUtils.isNotEmpty(accessCtrlApplHttp.getDirectory())) {
//            accessCtrlApplHttp.setDirectory(accessCtrlApplHttp.getDirectory().trim());
//        }
        return toAjax(accessCtrlApplService.updateAccessCtrlApplHttp(accessCtrlApplHttp));
    }

    /**
     * 删除HTTP访问控制
     */
    @PreAuthorize("@ss.hasPermi('access:appl:http:remove')")
    @Log(title = "HTTP访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accessCtrlApplService.deleteAccessCtrlApplHttpByIds(ids));
    }
}