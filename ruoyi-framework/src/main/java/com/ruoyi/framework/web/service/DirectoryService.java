package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.model.file.DirectoryNode;
import com.ruoyi.common.utils.file.FileUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DirectoryService {
//    @Cacheable(value = "directoryCache", key = "#path")
    public List<DirectoryNode> getDirectoryStructure(String path) {
        List<DirectoryNode> result = null;
        try {
            result = FileUtils.getDirectoryStructure(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
