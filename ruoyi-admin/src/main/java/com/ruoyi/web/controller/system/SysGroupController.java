package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysGroup;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysGroupService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "组别管理")
@RestController
@RequestMapping("/system/group")
public class SysGroupController extends BaseController {

    @Autowired
    private ISysGroupService groupService;

    /**
     * 查询组别列表
     */
//    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGroup sysGroup) {
        startPage();
        List<SysGroup> list = groupService.selectGroupList(sysGroup);
        return getDataTable(list);
    }

    /**
     * 新增组别
     */
//    @PreAuthorize("@ss.hasPermi('system:group:add')")
    @Log(title = "组别管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGroup sysGroup) {
        return toAjax(groupService.addGroup(sysGroup));
    }

    /**
     * 修改组别
     */
//    @PreAuthorize("@ss.hasPermi('system:group:edit')")
    @Log(title = "组别管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGroup sysGroup) {
        return toAjax(groupService.updateGroup(sysGroup));
    }

    /**
     * 删除组别
     */
//    @PreAuthorize("@ss.hasPermi('system:group:remove')")
    @Log(title = "组别管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds) {
        return toAjax(groupService.deleteGroup(groupIds));
    }
}