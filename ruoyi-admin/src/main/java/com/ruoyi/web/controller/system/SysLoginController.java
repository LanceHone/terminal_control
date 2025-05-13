package com.ruoyi.web.controller.system;

import com.ruoyi.access.mapper.AccessCtlLogsMapper;
import com.ruoyi.access.mapper.AccessMdbLogsMapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.auth.LoginBody;
import com.ruoyi.common.core.domain.model.auth.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/locked")
    public AjaxResult globalLocked(){
        AjaxResult ajax = AjaxResult.success();
        Map result = loginService.globalLocked();
        ajax.putAll(result);
        return ajax;
    }
    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        Map tokenResult = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        ajax.putAll(tokenResult);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions))
        {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);

        String size = dictDataService.selectDictDataById(118L).getDictValue();
        String rate = dictDataService.selectDictDataById(121L).getDictValue();

        double threshold = Double.parseDouble(size) * Double.parseDouble(rate) * 1024;

        Integer ctl = accessCtlLogsMapper.countSize();
        Integer mdb = accessMdbLogsMapper.countSize();

        if (3.7 + (ctl * 0.052272727) > threshold) {
            ajax.put("logSizeMsg", "访问控制日志已经达到阈值，请处理");
        } else if (3.7 + (mdb * 0.03542857142) > threshold) {
            ajax.put("logSizeMsg", "modbus日志已经达到阈值，请处理");
        }
        if (user.getPasswordUpdateTime() == null) {
            ajax.put("pwdReset", "密码剩余有效期：180天");
        }else {
            LocalDate lastUpdated = user.getPasswordUpdatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate();
            long between = ChronoUnit.DAYS.between(lastUpdated, LocalDate.now());
            ajax.put("pwdReset", "密码剩余有效期：" + (180 - between) + "天");
        }
        return ajax;
    }

    @Autowired
    ISysDictDataService dictDataService;
    @Autowired
    AccessCtlLogsMapper accessCtlLogsMapper;
    @Autowired
    AccessMdbLogsMapper accessMdbLogsMapper;
    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
