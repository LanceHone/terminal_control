package com.ruoyi.access.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访问控制对象
 */
public abstract class AccessPolicy extends BaseEntity {
    //策略守护线程id
    private long policyThreadId;

    public long getPolicyThreadId() {
        return policyThreadId;
    }

    public void setPolicyThreadId(long policyThreadId) {
        this.policyThreadId = policyThreadId;
    }
}
