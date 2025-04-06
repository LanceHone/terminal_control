//package com.ruoyi.access.components;
//
//import com.ruoyi.access.domain.AccessPolicy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Service
//public class PolicyEngine {
//    private final Map<String, List<AccessPolicy>> policyCache = new ConcurrentHashMap<>();
//
//    @Autowired
//    private PolicyRepository policyRepo;//TODO
//
//    // 每5秒刷新策略缓存
//    @Scheduled(fixedRate = 5000)
//    public void refreshCache() {
//        policyRepo.findAllEnabled().forEach(policy ->
//                policyCache.compute(policy.getProtocol(), (k, v) -> {
//                    if (v == null) return new CopyOnWriteArrayList<>(List.of(policy));
//                    v.add(policy);
//                    return v;
//                })
//        );
//    }
//
//    // 策略匹配核心逻辑
//    public boolean checkAccess(ProtocolContext ctx) {
//        List<AccessPolicy> policies = policyCache.getOrDefault(ctx.getProtocol(), Collections.emptyList());
//        if (policies.isEmpty()) return true; // 默认放行
//
//        return policies.stream().anyMatch(policy ->
//                matchIp(policy.getSrcIp(), ctx.getSrcIp()) &&
//                        matchPort(policy.getSrcPort(), ctx.getSrcPort()) &&
//                        matchModbus(policy, ctx)
//        );
//    }
//
//    private boolean matchIp(String policyIp, String requestIp) {
//        if (policyIp == null) return true;
//        return new CIDRUtils(policyIp).isInRange(requestIp);
//    }
//}