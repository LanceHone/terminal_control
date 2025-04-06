//package com.ruoyi.access.components;
//
//import com.ruoyi.access.service.IAccessCtrlInduModbusService;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class AccessControlInterceptor implements HandlerInterceptor {
//    @Autowired
//    private IAccessCtrlInduModbusService policyService;
////    private PolicyService policyService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 1. 获取请求信息
//        String protocol = "HTTP"; // 实际需解析协议类型
//        String srcIp = request.getRemoteAddr();
//        int destPort = request.getServerPort();
//
//        // 2. 查询匹配策略（优先白名单）
//        List<AccessControlPolicy> matchedPolicies = policyService.findMatchedPolicies(protocol, srcIp, destPort);
//
//        // 3. 执行策略判定
//        if (CollectionUtils.isEmpty(matchedPolicies)) {
////        if (matchedPolicies.isEmpty()) {
//            // 默认无策略时允许通信
//            logAccess(request, "ALLOW", "No policy matched");
//            return true;
//        } else {
//            boolean isAllowed = matchedPolicies.stream().anyMatch(AccessControlPolicy::isAllow);
//            if (isAllowed) {
//                logAccess(request, "ALLOW", "Matched allow policy");
//                return true;
//            } else {
//                logAccess(request, "DENY", "Matched deny policy");
//                response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
//                return false;
//            }
//        }
//    }
//
//    private void logAccess(HttpServletRequest request, String result, String reason) {
//        // 记录日志（需实现日志服务）
//        LogEntry log = new LogEntry(
//                LocalDateTime.now(),
//                request.getRemoteAddr(),
//                request.getServerPort(),
//                request.getProtocol(),
//                result,
//                reason
//        );
//        logService.save(log);
//    }
//}