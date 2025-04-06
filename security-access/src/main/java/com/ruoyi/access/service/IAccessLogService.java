//package com.ruoyi.access.service;
//
//import com.ruoyi.access.domain.model.modbus.ModbusFrame;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class IAccessLogService {
//    public void logRequest(ModbusFrame frame, boolean isAllowed) {
//        System.out.println("Access log: " + frame + ", isAllowed: " + isAllowed);
//        //TODO
////        ModbusAccessLog log = new ModbusAccessLog();
////        log.setTimestamp(LocalDateTime.now());
////        log.setRemoteIp(getRemoteIp(ctx));
////        log.setDeviceId(frame.getUnitId());
////        log.setFunctionCode(frame.getFunctionCode());
////        log.setRegisterAddr(parseRegister(frame.getData()));
////        log.setValue(parseValue(frame.getData()));
////        log.setIsAllowed(isAllowed);
////        log.setReason(isAllowed ? "Policy allowed" : "Policy denied");
////
////        logRepository.save(log);
//    }
//}
