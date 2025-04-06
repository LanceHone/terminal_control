package com.ruoyi.access.service.impl;

import com.ruoyi.access.domain.AccessPolicyApplTelnet;
import com.ruoyi.access.mapper.AccessCtrlApplTelnetMapper;
import com.ruoyi.access.service.IAccessCtrlApplTelnetService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TELNET访问控制 服务实现
 *
 * @author ruoyi
 */
@Service
public class AccessCtrlApplTelnetServiceImpl implements IAccessCtrlApplTelnetService {

    @Autowired
    private AccessCtrlApplTelnetMapper accessCtrlApplMapper;

    /**
     * 查询TELNET访问控制列表
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return TELNET访问控制集合
     */
    @Override
    @DataScope
    public List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetList(AccessPolicyApplTelnet accessCtrlApplTelnet) {
        return accessCtrlApplMapper.selectAccessCtrlApplTelnetList(accessCtrlApplTelnet);
    }

    /**
     * 根据ID查询TELNET访问控制信息
     *
     * @param id TELNET访问控制ID
     * @return TELNET访问控制信息
     */
    @Override
    public AccessPolicyApplTelnet selectAccessCtrlApplTelnetById(Long id) {
        return accessCtrlApplMapper.selectAccessCtrlApplTelnetById(id);
    }

    /**
     * 新增TELNET访问控制
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return 结果
     */
    @Override
    public int insertAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet) {
        // 校验唯一性
//        if (StringUtils.isNotNull(checkUnique(accessCtrlApplTelnet.getServerName(), accessCtrlApplTelnet.getDirectory()))) {
//            throw new ServiceException("服务名称和目录已存在，不能重复添加");
//        }
        accessCtrlApplTelnet.setCreateBy(SecurityUtils.getUsername());
        return accessCtrlApplMapper.insertAccessCtrlApplTelnet(accessCtrlApplTelnet);
    }

    /**
     * 修改TELNET访问控制
     *
     * @param accessCtrlApplTelnet TELNET访问控制信息
     * @return 结果
     */
    @Override
    public int updateAccessCtrlApplTelnet(AccessPolicyApplTelnet accessCtrlApplTelnet) {
        // 校验唯一性
//        if (StringUtils.isNotNull(checkUnique(accessCtrlApplTelnet.getServerName(), accessCtrlApplTelnet.getDirectory()))) {
//            throw new ServiceException("服务名称和目录已存在，不能重复添加");
//        }
        accessCtrlApplTelnet.setUpdateBy(SecurityUtils.getUsername());
        return accessCtrlApplMapper.updateAccessCtrlApplTelnet(accessCtrlApplTelnet);
    }

    /**
     * 删除TELNET访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccessCtrlApplTelnetByIds(Long[] ids) {
        return accessCtrlApplMapper.deleteAccessCtrlApplTelnetByIds(ids);
    }

    /**
     * 校验服务名称和目录是否唯一
     *
     * @param serverName 服务名称
     * @param directory 目录
     * @return 结果
     */
    @Override
    public AccessPolicyApplTelnet checkUnique(String serverName, String directory) {
        return accessCtrlApplMapper.checkUnique(serverName, directory);
    }

    /**
     * 根据状态查询TELNET访问控制列表
     *
     * @param status 状态
     * @return TELNET访问控制集合
     */
    @Override
    public List<AccessPolicyApplTelnet> selectAccessCtrlApplTelnetListByStatus(String status) {
        return accessCtrlApplMapper.selectAccessCtrlApplTelnetListByStatus(status);
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