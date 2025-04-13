package com.ruoyi.web.controller.monitor;

import cn.hutool.core.util.RuntimeUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.web.domain.Server;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 服务器监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @PostMapping("/time/sync")
    @Log(title = "系统时间同步设置", businessType = BusinessType.UPDATE)
    public AjaxResult syncSwitch(@RequestParam String time) throws Exception {
        RuntimeUtil.execForStr("sudo systemctl enable --now chronyd");
        RuntimeUtil.execForStr("sudo date -s " + time);
        RuntimeUtil.execForStr("timedatectl set-ntp true");
        return AjaxResult.success("设置成功");
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @PostMapping("/time/setTz")
    @Log(title = "系统时区设置", businessType = BusinessType.UPDATE)
    public AjaxResult setTz() throws Exception {
        RuntimeUtil.execForStr("timedatectl set-timezone Asia/Shanghai");
        return AjaxResult.success("设置成功");
    }
    @Autowired
    private ServerConfig serverConfig;
    @PostMapping("/upload")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @Log(title = "上传更新包", businessType = BusinessType.UPDATE)
    public AjaxResult uploadFile(MultipartFile file,String md5) throws Exception {
        try (InputStream in = file.getInputStream()){
            String s = DigestUtils.md5DigestAsHex(in);
            if(!s.equals(md5)){
                return AjaxResult.error("md5校验失败");
            }
        }
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());

            String path = filePath + fileName.replace("", "/profile/upload/");
            RuntimeUtil.execForStr("tar -zxvf "+ path + " -C " + "/home/dap/rtfk");
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
