package com.ruoyi.web.controller.monitor;

import cn.hutool.core.util.RuntimeUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.domain.Server;
import com.ruoyi.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/time")
    public AjaxResult now() throws Exception {
        AjaxResult ret = AjaxResult.success();
        // List<String> timedatectl = RuntimeUtil.execForLines("timedatectl");
        // for (String line : timedatectl) {
        //     if (line.contains(":")) {
        //         String[] parts = line.split(":");
        //         String key = parts[0].trim();
        //         String value = parts[1].trim();
        //         ret.put(key, value);
        //     }
        // }
        ret.put("test", "test");
        return ret;
    }

    @Autowired
    ISysDictDataService dictDataService;

    @PostMapping("/time/sync")
    @Log(title = "系统时间同步设置", businessType = BusinessType.UPDATE)
    public AjaxResult syncSwitch(@RequestBody SysDictData dictData) throws Exception {
        dictDataService.updateDictData(dictData);
        RuntimeUtil.execForStr("echo 'server " + dictData.getDictValue() + " iburst' > /etc/chrony/sources.d/local-ntp-server.sources");
        RuntimeUtil.execForStr("chronyc reload sources");
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
    public AjaxResult uploadFile(MultipartFile file, String md5) throws Exception {
        try (InputStream in = file.getInputStream()) {
            String s = DigestUtils.md5DigestAsHex(in);
            if (!s.equals(md5)) {
                return AjaxResult.error("md5校验失败");
            }
        }
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success("上传成功");
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());

            String path = filePath + fileName.replace("", "/profile/upload/");
            RuntimeUtil.execForStr("mv " + path + " " + "/home/dap/rtfk/ruoyi-admin.jar");
            RuntimeUtil.execForStr("cp " + path + " " + "/root/ruoyi-admin.jar");
            RuntimeUtil.execForStr("echo " + md5 + " > /root/md5.txt");
            // RuntimeUtil.execForStr("tar -xf /root/dist.tar -C /path/to/nginx/html/");//fixme 发布时改路径
            // RuntimeUtil.execForStr("tar -xf /home/dap/rtfk/nginx/dist.tar");
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }).start();
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
