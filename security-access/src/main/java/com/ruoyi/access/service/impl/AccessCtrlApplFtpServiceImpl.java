package com.ruoyi.access.service.impl;

import com.ruoyi.access.domain.AccessPolicyApplFtp;
import com.ruoyi.access.mapper.AccessCtrlApplFtpMapper;
import com.ruoyi.access.service.IAccessCtrlApplFtpService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * FTP访问控制 服务实现
 *
 * @author ruoyi
 */
@Service
public class AccessCtrlApplFtpServiceImpl implements IAccessCtrlApplFtpService {

    @Autowired
    private AccessCtrlApplFtpMapper accessCtrlApplMapper;

    /**
     * 查询FTP访问控制列表
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return FTP访问控制集合
     */
    @Override
    @DataScope
    public List<AccessPolicyApplFtp> selectAccessCtrlApplFtpList(AccessPolicyApplFtp accessCtrlApplFtp) {
        return accessCtrlApplMapper.selectAccessCtrlApplFtpList(accessCtrlApplFtp);
    }

    /**
     * 根据ID查询FTP访问控制信息
     *
     * @param id FTP访问控制ID
     * @return FTP访问控制信息
     */
    @Override
    public AccessPolicyApplFtp selectAccessCtrlApplFtpById(Long id) {
        return accessCtrlApplMapper.selectAccessCtrlApplFtpById(id);
    }

    /**
     * 新增FTP访问控制
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return 结果
     */
    @Override
    public int insertAccessCtrlApplFtp(AccessPolicyApplFtp accessCtrlApplFtp) {
        // 校验唯一性
        if (StringUtils.isNotNull(checkUnique(accessCtrlApplFtp.getServerName(), accessCtrlApplFtp.getDirectory()))) {
            throw new ServiceException("服务名称和目录已存在，不能重复添加");
        }
        accessCtrlApplFtp.setCreateBy(SecurityUtils.getUsername());
        return accessCtrlApplMapper.insertAccessCtrlApplFtp(accessCtrlApplFtp);
    }

    /**
     * 修改FTP访问控制
     *
     * @param accessCtrlApplFtp FTP访问控制信息
     * @return 结果
     */
    @Override
    public int updateAccessCtrlApplFtp(AccessPolicyApplFtp accessCtrlApplFtp) {
        // 校验唯一性
        if (StringUtils.isNotNull(checkUnique(accessCtrlApplFtp.getServerName(), accessCtrlApplFtp.getDirectory()))) {
            throw new ServiceException("服务名称和目录已存在，不能重复添加");
        }
        accessCtrlApplFtp.setUpdateBy(SecurityUtils.getUsername());
        return accessCtrlApplMapper.updateAccessCtrlApplFtp(accessCtrlApplFtp);
    }

    /**
     * 删除FTP访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccessCtrlApplFtpByIds(Long[] ids) {
        return accessCtrlApplMapper.deleteAccessCtrlApplFtpByIds(ids);
    }

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    @Override
    public AccessPolicyApplFtp checkUnique(String serverName, String directory) {
        return accessCtrlApplMapper.checkUnique(serverName, directory);
    }

    /**
     * 根据状态查询FTP访问控制列表
     *
     * @param status 状态
     * @return FTP访问控制集合
     */
    @Override
    public List<AccessPolicyApplFtp> selectAccessCtrlApplFtpListByStatus(String status) {
        return accessCtrlApplMapper.selectAccessCtrlApplFtpListByStatus(status);
    }

    @Override
    public Boolean enablePolicy(Long policyId) {
        return null;
    }

    @Override
    public Boolean disablePolicy(Long policyId) {
        return null;
    }

    @Override
    public boolean isAccessAllowed() {
        return false;
    }
}