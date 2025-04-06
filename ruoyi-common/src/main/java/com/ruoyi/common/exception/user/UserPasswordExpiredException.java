package com.ruoyi.common.exception.user;

/**
 * 用户密码过期异常
 *
 * @author ruoyi
 */
public class UserPasswordExpiredException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordExpiredException() {
        this("user.password.expired");
    }

    public UserPasswordExpiredException(String msg) {
        this(msg, null);
    }

    public UserPasswordExpiredException(String msg, Object[] args) {
        super(msg, args);
    }
}
