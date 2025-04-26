package com.ruoyi.web.controller.access;

import cn.hutool.core.util.RuntimeUtil;
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

import java.util.ArrayList;
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
    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping("/list")
    public AjaxResult list(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        List<AccessPolicyNetworkTcp> list = accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpList(accessCtrlNetworkTcp);
        return success(list);
    }

    /**
     * 根据ID获取网络层访问控制详细信息
     */
    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpById(id));
    }

    /**
     * 新增网络层访问控制
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "新增网络层访问控制", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        // if (StringUtils.isNotNull(accessCtrlNetworkTcpService.checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
        //     return error("新增网络层访问控制失败，源IP和目的IP已存在");
        // }
        accessCtrlNetworkTcp.setCreateBy(getUsername());
        if (!System.getProperty("os.name").contains("Win") && accessCtrlNetworkTcp.getStatus().equals("0")) {
            accessCtrlNetworkTcpService.updateWhiteList(accessCtrlNetworkTcp, "add");
        }
        return toAjax(accessCtrlNetworkTcpService.insertAccessCtrlNetworkTcp(accessCtrlNetworkTcp));
    }

    /**
     * 修改网络层访问控制
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "修改网络层访问控制", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        if (StringUtils.isNotNull(accessCtrlNetworkTcpService.checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
            return error("修改网络层访问控制失败，源IP和目的IP已存在");
        }
        AccessPolicyNetworkTcp old = accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpById(accessCtrlNetworkTcp.getId());
        AccessPolicyNetworkTcp newAcc = accessCtrlNetworkTcp;
        accessCtrlNetworkTcp.setUpdateBy(getUsername());
        if (!System.getProperty("os.name").contains("Win")) {
            if ("0".equals(old.getStatus())) accessCtrlNetworkTcpService.updateWhiteList(old, "delete");
            if("0".equals(newAcc.getStatus()))accessCtrlNetworkTcpService.updateWhiteList(accessCtrlNetworkTcp, "add");
        }
        return toAjax(accessCtrlNetworkTcpService.updateAccessCtrlNetworkTcp(accessCtrlNetworkTcp));
    }

    /**
     * 修改策略状态
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "修改策略状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult updateStatus(@RequestBody AccessPolicyNetworkTcp modbus) {
        AccessPolicyNetworkTcp old = accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpById(modbus.getId());
        modbus.setUpdateBy(SecurityUtils.getUsername());
        if (!System.getProperty("os.name").contains("Win")) {
            accessCtrlNetworkTcpService.updateWhiteList(modbus, "0".equals(modbus.getStatus()) ? "add" : "delete");
            if ("0".equals(old.getStatus())) accessCtrlNetworkTcpService.updateWhiteList(old, "delete");
            if("0".equals(modbus.getStatus()))accessCtrlNetworkTcpService.updateWhiteList(old, "add");
        }
        return toAjax(accessCtrlNetworkTcpService.updateStatus(modbus));
    }

    /**
     * 删除网络层访问控制
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "删除网络层访问控制策略", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        AccessPolicyNetworkTcp accessCtrlNetworkTcp = accessCtrlNetworkTcpService.selectAccessCtrlNetworkTcpById(id);
        if (!System.getProperty("os.name").contains("Win") && "0".equals(accessCtrlNetworkTcp.getStatus())) {
            accessCtrlNetworkTcpService.updateWhiteList(accessCtrlNetworkTcp, "delete");
        }
        return toAjax(accessCtrlNetworkTcpService.deleteAccessCtrlNetworkTcpByIds(new Long[]{id}));
    }

    /**
     * 批量删除网络层访问控制
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "网络层访问控制", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult removeBatch(@RequestBody Long[] ids) {
        return toAjax(accessCtrlNetworkTcpService.deleteAccessCtrlNetworkTcpByIds(ids));
    }

    /**
     * 批量删除网络层访问控制
     */
    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "应用层-http访问控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/http/{enable}")
    public AjaxResult http(@PathVariable Boolean enable) {
        String cmd0;
        String cmd;
        if (!enable) {
            cmd0 = "iptables -A PORT_BLACK_LIST -p tcp --dport 80 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -A PORT_BLACK_LIST -p tcp --dport 80 -j DROP";
        } else {
            cmd0 = "iptables -D PORT_BLACK_LIST -p tcp --dport 80 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -D PORT_BLACK_LIST -p tcp --dport 80 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        httpStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "应用层-telnet访问控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/telnet/{enable}")
    public AjaxResult telnet(@PathVariable Boolean enable) {
        String cmd0;
        String cmd;
        if (!enable) {
            cmd0 = "iptables -A PORT_BLACK_LIST -p tcp --dport 23 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -A PORT_BLACK_LIST -p tcp --dport 23 -j DROP";
        } else {
            cmd0 = "iptables -D PORT_BLACK_LIST -p tcp --dport 23 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -D PORT_BLACK_LIST -p tcp --dport 23 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        telnetStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "应用层-ftp访问控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/ftp/{enable}")
    public AjaxResult ftp(@PathVariable Boolean enable) {
        String cmd0;
        String cmd;
        if (!enable) {
            cmd0 = "iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j DROP";
        } else {
            cmd0 = "iptables -D PORT_BLACK_LIST -p tcp --dport 21 -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -D PORT_BLACK_LIST -p tcp --dport 21 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        ftpStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping("/app/status")
    public AjaxResult appStatus() {
        List<AppStatus> appStatuses = new ArrayList<>();
        appStatuses.add(new AppStatus("http", httpStatus));
        appStatuses.add(new AppStatus("telnet", telnetStatus));
        appStatuses.add(new AppStatus("ftp", ftpStatus));
        return AjaxResult.success(appStatuses);
    }

    @PreAuthorize("@ss.hasRole('security')")
    @GetMapping("/flood/status")
    public AjaxResult floodStatus() {
        List<AppStatus> appStatuses = new ArrayList<>();
        appStatuses.add(new AppStatus("启用网络层白名单", netStatus));
        appStatuses.add(new AppStatus("抗SYN_FLOOD攻击", synStatus));
        appStatuses.add(new AppStatus("抗UDP_FLOOD攻击", udpStatus));
        appStatuses.add(new AppStatus("抗ICMP_FLOOD攻击", icmpStatus));
        appStatuses.add(new AppStatus("抗PING_OF_DEATH攻击", pingStatus));
        return AjaxResult.success(appStatuses);
    }

    boolean httpStatus = true;
    boolean telnetStatus = true;
    boolean ftpStatus = true;

    boolean netStatus = false;
    boolean synStatus = false;
    boolean udpStatus = false;
    boolean icmpStatus = false;
    boolean pingStatus = false;

    static class AppStatus {
        String name;
        Boolean enable;

        public AppStatus() {
        }

        public AppStatus(String name, Boolean enable) {
            this.name = name;
            this.enable = enable;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "网络访问控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/net/{enable}")
    public AjaxResult net(@PathVariable Boolean enable) {
        String cmd0;
        String cmd;
        if (!enable) {
            cmd0 = "iptables -D NET_WHITE_LIST -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -D NET_WHITE_LIST -j DROP";
        } else {
            cmd0 = "iptables -A NET_WHITE_LIST -j LOG --log-prefix \"[DROP]\" --log-level 4";
            cmd = "iptables -A NET_WHITE_LIST -j DROP";
        }
        logger.info(cmd0);
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd0);
        RuntimeUtil.execForStr(cmd);
        netStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "抗PING_OF_DEATH控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/ping/{enable}")
    public AjaxResult ping(@PathVariable Boolean enable) {
        String cmd;
        if (!enable) {
            cmd = "iptables -D INPUT -p icmp --icmp-type echo-request -m length --length 100:65535 -j DROP";
        } else {
            cmd = "iptables -I INPUT 1 -p icmp --icmp-type echo-request -m length --length 100:65535 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        pingStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "抗ICMP_FLOOD控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/icmp/{enable}")
    public AjaxResult icmp(@PathVariable Boolean enable) {
        String cmd;
        if (!enable) {
            cmd = "iptables -D ICMP_FLOOD_CHECK -m recent --name icmprate --rcheck --seconds 1 --hitcount 11 -j DROP";
        } else {
            cmd = "iptables -A ICMP_FLOOD_CHECK -m recent --name icmprate --rcheck --seconds 1 --hitcount 11 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        icmpStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "抗UDP_FLOOD控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/udp/{enable}")
    public AjaxResult udp(@PathVariable Boolean enable) {

        String cmd;
        if (!enable) {
            cmd = "iptables -D UDP_FLOOD_CHECK -m recent --name udprate --rcheck --seconds 1 --hitcount 11 -j DROP";
        } else {
            cmd = "iptables -A UDP_FLOOD_CHECK -m recent --name udprate --rcheck --seconds 1 --hitcount 11 -j DROP";
        }
        logger.info(cmd);
        RuntimeUtil.execForStr(cmd);
        udpStatus = enable;
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasRole('security')")
    @Log(title = "抗SYN_FLOOD控制开关", businessType = BusinessType.DELETE)
    @PostMapping("/syn/{enable}")
    public AjaxResult syn(@PathVariable Boolean enable) {

        String cmd;
        String cmd1;
        if (!enable) {
            cmd = "sysctl -w net.ipv4.tcp_syncookies=0";
            cmd1 =  "iptables -D SYN_FLOOD_CHECK -m recent --name synrate --rcheck --seconds 1 --hitcount 6 -j DROP";
        } else {
            cmd = "sysctl -w net.ipv4.tcp_syncookies=1";
            cmd1 =  "iptables -A SYN_FLOOD_CHECK -m recent --name synrate --rcheck --seconds 1 --hitcount 6 -j DROP";
        }
        logger.info(cmd);
        // RuntimeUtil.execForStr(cmd);
        // RuntimeUtil.execForStr("sysctl -p");//重启生效
        RuntimeUtil.execForStr(cmd1);
        synStatus = enable;
        return AjaxResult.success();
    }
}