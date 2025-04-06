//package com.ruoyi.access.interceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import com.example.networkpolicy.service.AccessControlService;
//
//@Component
//public class NetworkAccessInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private AccessControlService accessControlService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 获取请求的相关信息
//        String sourceIp = request.getRemoteAddr();
//        int sourcePort = request.getLocalPort();
//        String destinationIp = request.getLocalAddr();
//        int destinationPort = request.getServerPort();
//
//        // 调用服务检查访问控制策略
//        boolean allowed = accessControlService.isAccessAllowed(sourceIp, sourcePort, destinationIp, destinationPort);
//
//        if (!allowed) {
//            // 如果访问被拒绝，返回相应的错误信息
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied by network policy");
//            return false;
//        }
//
//        return true;
//    }
//}