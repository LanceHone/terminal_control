package com.ruoyi.access.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.ruoyi.access.domain.AccessControlLogItem;
import com.ruoyi.access.domain.AccessPolicyNetworkTcp;
import com.ruoyi.access.mapper.AccessCtrlNetworkTcpMapper;
import com.ruoyi.access.service.IAccessCtrlNetworkTcpService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络层访问控制 服务实现
 *
 * @author ruoyi
 */
@Service
public class AccessCtrlNetworkTcpServiceImpl implements IAccessCtrlNetworkTcpService {

    @Autowired
    private AccessCtrlNetworkTcpMapper accessCtrlNetworkTcpMapper;

    /**
     * 查询网络层访问控制列表
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 网络层访问控制集合
     */
    @Override
    @DataScope
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpList(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpList(accessCtrlNetworkTcp);
    }

    /**
     * 根据ID查询网络层访问控制信息
     *
     * @param id 网络层访问控制ID
     * @return 网络层访问控制信息
     */
    @Override
    public AccessPolicyNetworkTcp selectAccessCtrlNetworkTcpById(Long id) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpById(id);
    }

    /**
     * 新增网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    @Override
    public int insertAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        // 校验唯一性
        // if (StringUtils.isNotNull(checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
        //     throw new ServiceException("源IP和目的IP已存在，不能重复添加");
        // }
        accessCtrlNetworkTcp.setCreateBy(SecurityUtils.getUsername());
        return accessCtrlNetworkTcpMapper.insertAccessCtrlNetworkTcp(accessCtrlNetworkTcp);
    }

    /**
     * 修改网络层访问控制
     *
     * @param accessCtrlNetworkTcp 网络层访问控制信息
     * @return 结果
     */
    @Override
    public int updateAccessCtrlNetworkTcp(AccessPolicyNetworkTcp accessCtrlNetworkTcp) {
        // 校验唯一性
        // if (StringUtils.isNotNull(checkIpUnique(accessCtrlNetworkTcp.getSourceIp(), accessCtrlNetworkTcp.getTargetIp()))) {
        //     throw new ServiceException("源IP和目的IP已存在，不能重复添加");
        // }
        accessCtrlNetworkTcp.setUpdateBy(SecurityUtils.getUsername());
        return accessCtrlNetworkTcpMapper.updateAccessCtrlNetworkTcp(accessCtrlNetworkTcp);
    }

    @Override
    public int updateStatus(AccessPolicyNetworkTcp modbus) {
        return accessCtrlNetworkTcpMapper.updateStatus(modbus);
    }

    /**
     * 删除网络层访问控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccessCtrlNetworkTcpByIds(Long[] ids) {
        return accessCtrlNetworkTcpMapper.deleteAccessCtrlNetworkTcpByIds(ids);
    }

    /**
     * 校验源IP和目的IP是否唯一
     *
     * @param sourceIp 源IP
     * @param targetIp 目的IP
     * @return 结果
     */
    @Override
    public AccessPolicyNetworkTcp checkIpUnique(String sourceIp, String targetIp) {
        return accessCtrlNetworkTcpMapper.checkIpUnique(sourceIp, targetIp);
    }

    /**
     * 根据状态查询网络层访问控制列表
     *
     * @param status 状态
     * @return 网络层访问控制集合
     */
    @Override
    public List<AccessPolicyNetworkTcp> selectAccessCtrlNetworkTcpListByStatus(String status) {
        return accessCtrlNetworkTcpMapper.selectAccessCtrlNetworkTcpListByStatus(status);
    }

    @Override
    public boolean isAccessAllowed(String srcIp, int srcPort, String dstIp, int dstPort) {
        return accessCtrlNetworkTcpMapper.isAccessAllowed(srcIp, srcPort, dstIp, dstPort);
    }

    @Override
    public boolean checkAccess(Object msg) {
        return false;
    }

    @Override
    public Boolean enablePolicy(Long policyId) {
        return null;
    }

    @Override
    public Boolean disablePolicy(Long policyId) {
        return null;
    }

    @Value("classpath:/init.sh")
    Resource initShell;
    private static final String DIR = "/opt/security/";
    private static final String LOG_DIR = "/opt/security/log/";
    private static final String LOG_FILE = "/opt/security/log/block_log.txt";
    private static final Logger logger = LoggerFactory.getLogger(AccessCtrlNetworkTcpServiceImpl.class);

    // 初始化和销毁
    @PostConstruct
    public void init() throws IOException {
        if (System.getProperty("os.name").contains("Win"))  return;
        if (!new File(DIR).exists()) new File(DIR).mkdirs();
        new File(DIR + "init.sh").createNewFile();
        IoUtil.copy(initShell.getInputStream(), Files.newOutputStream(Paths.get(DIR + "init.sh")));
        String stdout = RuntimeUtil.execForStr("bash " + DIR + "init.sh");
        // 查询策略并逐一添加
        List<AccessPolicyNetworkTcp> list = selectAccessCtrlNetworkTcpListByStatus("0");
        for (AccessPolicyNetworkTcp accessCtrlNetworkTcp : list) {
            updateWhiteList(accessCtrlNetworkTcp, "add");
        }
    }

    // iptables -A INPUT -p tcp -s 192.168.1.100 --sport 12345 -d 10.0.0.1 --dport 80 -j DROP
    public void updateWhiteList(AccessPolicyNetworkTcp accessCtrlNetworkTcp, String operation){
        StringBuilder cmd = new StringBuilder("iptables");
        if ("add".equals(operation)) {
            cmd.append(" -I NET_WHITE_LIST 1");
        } else if ("delete".equals(operation)) {
            cmd.append(" -D NET_WHITE_LIST");
        }
        cmd.append(" -s ").append(accessCtrlNetworkTcp.getSourceIp());
        if (accessCtrlNetworkTcp.getSourcePort() != 0) {
            cmd.append(" --sport ").append(accessCtrlNetworkTcp.getSourcePort());
        }
        cmd.append(" -d ").append(accessCtrlNetworkTcp.getTargetIp());
        if (accessCtrlNetworkTcp.getTargetPort() != 0) {
            cmd.append(" --dport ").append(accessCtrlNetworkTcp.getTargetPort());
        }
        cmd.append(" -j RETURN");
        logger.info(cmd.toString());
        RuntimeUtil.execForStr(cmd.toString());
    }


    @Scheduled(cron = "0 0/5 * * * ? ")
    public List<AccessControlLogItem> parseLogFile() {
        String filePath = "/var/access/acc_ctl.log";
        Path path = Paths.get(filePath);
        List<AccessControlLogItem> entries = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "^(\\S+)\\s+.*?(\\[ACCEPT\\]|\\[DROP\\]).*?SRC=(\\S+) DST=(\\S+) .*?SPT=(\\d+) DPT=(\\d+)"
        );

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String timestamp = matcher.group(1);
                    String status = matcher.group(2);
                    String srcIP = matcher.group(3);
                    String dstIP = matcher.group(4);
                    int srcPort = Integer.parseInt(matcher.group(5));
                    int dstPort = Integer.parseInt(matcher.group(6));

                    entries.add(new AccessControlLogItem(timestamp, status, srcIP, dstIP, srcPort, dstPort));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 清空文件内容
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            // 写入空字符串即可清空
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

}