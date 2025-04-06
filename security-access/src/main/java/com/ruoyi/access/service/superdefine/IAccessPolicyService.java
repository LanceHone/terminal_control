package com.ruoyi.access.service.superdefine;

/**
 * 访问策略服务层
 */
public abstract interface IAccessPolicyService {
    /**
     * 启用策略
     * @param policyId
     * @return
     */
    public Boolean enablePolicy(Long policyId);

    /**
     * 禁用策略
     * @param policyId
     * @return
     */
    public Boolean disablePolicy(Long policyId);
}
