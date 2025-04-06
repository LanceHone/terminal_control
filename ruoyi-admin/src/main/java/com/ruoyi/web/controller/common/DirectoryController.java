package com.ruoyi.web.controller.common;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.file.DirectoryNode;
import com.ruoyi.framework.web.service.DirectoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文件目录
 */
@RestController
@RequestMapping("/directory")
public class DirectoryController extends BaseController {

    @Resource
    private DirectoryService directoryService;

    @GetMapping("/get")
    public AjaxResult getDirectory(@RequestParam String path) {
        // 调用文件系统工具类获取目录结构
        List<DirectoryNode> directoryData = directoryService.getDirectoryStructure(path);
        return success(directoryData);
    }
}