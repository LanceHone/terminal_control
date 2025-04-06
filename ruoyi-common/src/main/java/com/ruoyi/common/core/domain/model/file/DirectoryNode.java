package com.ruoyi.common.core.domain.model.file;

import java.util.List;

/**
 * 文件系统目录节点
 */
public class DirectoryNode {
    private String name;
    private String path;
    private boolean isDirectory;
    private boolean readable;
    private List<DirectoryNode> children;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<DirectoryNode> getChildren() {
        return children;
    }

    public void setChildren(List<DirectoryNode> children) {
        this.children = children;
    }

    public boolean getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean getReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }
}
