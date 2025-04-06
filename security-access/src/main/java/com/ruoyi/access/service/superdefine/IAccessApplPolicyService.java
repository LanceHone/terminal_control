package com.ruoyi.access.service.superdefine;

/**
 * 访问控制策略 服务层
 */
public abstract interface IAccessApplPolicyService  extends IAccessPolicyService {
    /**
     * 检查是否允许访问
     *
     * @return true 如果允许访问，false 否则
     */
    public boolean isAccessAllowed();
}
