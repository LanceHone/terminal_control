package com.ruoyi.access.mapper;

import com.ruoyi.access.domain.AccessControlLogItem;

import java.util.List;

public interface AccessControlLogItemMapper {

    // scp -P 22000 /root/temp/timebargain-1.0.0.jar root@211.149.225.11:/root
    // scp -P 22000 /root/temp/api_server.py root@211.149.225.11:/root
    // r9zoZOpJl0Xb
    List<AccessControlLogItem> selectList(AccessControlLogItem item);

    void insertOne(AccessControlLogItem item);
}
