package com.ruoyi.framework.web.service;

import javax.annotation.Resource;

import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.auth.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.constant.UserConstants.LOCK_TIME;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {
    @Value(value = "3")
    private int maxRetryCount;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     * @see UserDetailsServiceImpl#loadUserByUsername
     */
    public Map login(String username, String password, String code, String uuid) {
        // 验证码校验
        // validateCaptcha(username, code, uuid);
        // 检查是否被锁定
        if (isUserLocked(username)) {//Fixme
            throw new LockedException("账户已被锁定，请稍后再试！");
        }
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            /** 该方法会去调用 UserDetailsServiceImpl#loadUserByUsername */
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                // 记录登录失败信息
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//                recordLoginFailure(username);
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        // 登录成功，清除失败记录
        clearLoginFailure(username);

        // 检查密码是否过期
        // Fixme 这个逻辑以及判断是否是初始化密码应该放到try里；前端捕捉此异常；然后将修改密码的接口放入白名单。
        boolean isPasswordExpired = checkPasswordExpiration(username);
        if (isPasswordExpired) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.expired")));
//            throw new UserPasswordExpiredException();//Fixme
//            throw new UserPasswordExpiredException("密码已过期，请修改密码！");
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 是否是初始化密码
        Boolean pwdReset = (!isPasswordExpired) &&
                (!SecurityUtils.matchesPassword(UserConstants.PASSWORD_DEFAULT, loginUser.getUser().getPassword()));//TODO 从配置表中

        // 生成token
        String token = tokenService.createToken(loginUser);

        Map result = new HashMap();
        result.put("token", token);
        result.put("pwdExpired", isPasswordExpired);
        result.put("pwdReset", pwdReset);
        return result;
    }

    /**
     * 检查密码是否过期
     *
     * @param username
     * @return
     */
    private boolean checkPasswordExpiration(String username) {
        SysUser user = userService.selectUserByUserName(username);
        if (user == null || user.getPasswordUpdatedTime() == null) {
            return false; // 如果没有记录密码更新时间，则认为未过期
        }

        LocalDateTime lastUpdated = user.getPasswordUpdatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // return lastUpdated.plusDays(UserConstants.PASSWORD_EXPIRATION_DAYS).isBefore(LocalDateTime.now());//fixme 发布时使用这一行
        return lastUpdated.plusSeconds(UserConstants.PASSWORD_EXPIRATION_DAYS).isBefore(LocalDateTime.now());
    }

    private boolean isUserLocked(String username) {
        Integer retryCount = redisCache.getCacheObject(CacheConstants.PWD_ERR_CNT_KEY + username);
        if (retryCount == null) {
            retryCount = 0;
        }
        return retryCount >= Integer.valueOf(maxRetryCount).intValue();

    }

    private void recordLoginFailure(String username) {
        String key = UserConstants.LOGIN_FAIL_KEY + username;
        Long attempts = redisCache.getCacheMapValue(key, UserConstants.ATTEMPTS);
        if (attempts == null) {
            attempts = 0L;
        }
        attempts++;

        Boolean locked = attempts >= UserConstants.MAX_ATTEMPTS;
        if (locked) {
            // 设置锁定时间
            SysUser user = userService.selectUserByUserName(username);
            if (user != null) {
                user.setLockDate(new Date());
                userService.updateUser(user);

                throw new UserPasswordRetryLimitExceedException(UserConstants.MAX_ATTEMPTS.intValue(), UserConstants.LOCK_TIME_MINUTES.intValue());
//                throw new UserPasswordRetryLimitExceedException("账户已被锁定，请稍后再试！");
//                throw new LockedException("账户已被锁定，请稍后再试！");
            }
//            LocalDateTime lockEndTime = LocalDateTime.now().plusMinutes(UserConstants.LOCK_TIME_MINUTES);
//            redisCache.setCacheMapValue(key, LOCK_TIME, lockEndTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        }
        redisCache.setCacheMapValue(key, UserConstants.ATTEMPTS, attempts);
        redisCache.expire(key, UserConstants.RESET_TIME_MINUTES, TimeUnit.MINUTES);
    }

    private void clearLoginFailure(String username) {
        String key = UserConstants.LOGIN_FAIL_KEY + username;
        redisCache.deleteObject(key);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
//        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//            // 记录登录失败次数
////            recordLoginFailure(username);
//            throw new UserPasswordNotMatchException();
//        }
//         用户名不在指定范围内 错误
//        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
//                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//            throw new UserPasswordNotMatchException();
//        }
        // IP黑名单校验
        String blackStr = configService.selectConfigValueByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    public Map globalLocked() {
        Object globalLocked = redisCache.getCacheObject(CacheConstants.PWD_ERR_GLOBAL_LOCK_KEY);
        Map result = new HashMap();
        result.put("globalLocked", globalLocked != null);
        return result;
    }
}
