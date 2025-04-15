package com.ruoyi.web.controller.access;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.access.domain.AccessCtlLogs;
import com.ruoyi.access.domain.AccessMdbLogs;
import com.ruoyi.access.mapper.AccessMdbLogsMapper;
import com.ruoyi.access.service.IAccessMdbLogsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * modbus控制日志Controller
 * 
 * @author ruoyi
 * @date 2025-04-15
 */
@RestController
@RequestMapping("/access/logs/mdb")
public class AccessMdbLogsController extends BaseController
{
    @Autowired
    private IAccessMdbLogsService accessMdbLogsService;

    /**
     * 查询modbus控制日志列表
     */
    @PreAuthorize("@ss.hasPermi('access:logs:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccessMdbLogs accessMdbLogs)
    {
        startPage();
        List<AccessMdbLogs> list = accessMdbLogsService.selectAccessMdbLogsList(accessMdbLogs);
        return getDataTable(list);
    }

    /**
     * 导出modbus控制日志列表
     */
    @PreAuthorize("@ss.hasPermi('access:logs:export')")
    @Log(title = "modbus控制日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccessMdbLogs accessMdbLogs)
    {
        List<AccessMdbLogs> list = accessMdbLogsService.selectAccessMdbLogsList(accessMdbLogs);
        ExcelUtil<AccessMdbLogs> util = new ExcelUtil<AccessMdbLogs>(AccessMdbLogs.class);
        util.exportExcel(response, list, "modbus控制日志数据");
    }

    /**
     * 获取modbus控制日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('access:logs:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(accessMdbLogsService.selectAccessMdbLogsById(id));
    }

    /**
     * 新增modbus控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:add')")
    @Log(title = "modbus控制日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccessMdbLogs accessMdbLogs)
    {
        return toAjax(accessMdbLogsService.insertAccessMdbLogs(accessMdbLogs));
    }

    /**
     * 修改modbus控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:edit')")
    @Log(title = "modbus控制日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccessMdbLogs accessMdbLogs)
    {
        return toAjax(accessMdbLogsService.updateAccessMdbLogs(accessMdbLogs));
    }

    /**
     * 删除modbus控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "modbus控制日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accessMdbLogsService.deleteAccessMdbLogsByIds(ids));
    }

    @Autowired
    AccessMdbLogsMapper accessMdbLogsMapper;
    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "清空modbus控制日志", businessType = BusinessType.DELETE)
    @PostMapping("/clear")
    public AjaxResult clear()
    {
        accessMdbLogsMapper.clear();
        return AjaxResult.success();
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void collect() throws IOException {
        File file = new File("/var/access/mdb.log");

        if (!file.exists()) return;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AccessMdbLogs mdbLog = new AccessMdbLogs();
                String[] parts = line.split(" - ", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("无效的日志格式: " + line);
                }

                mdbLog.setTs(LocalDateTime.parse(parts[0], formatter));
                // 解析 key=value
                String keyValuePart = parts[1];
                Map<String, String> fields = new LinkedHashMap<>();

                Matcher kvMatcher = Pattern.compile("(\\w+)=([^,\\s]*)").matcher(keyValuePart);
                while (kvMatcher.find()) {
                    fields.put(kvMatcher.group(1), kvMatcher.group(2));
                }
                mdbLog.setTid(fields.get("TID"));
                mdbLog.setPid(fields.get("PID"));
                mdbLog.setLen(fields.get("LEN"));
                mdbLog.setUid(fields.get("UID"));
                mdbLog.setFunc(fields.get("FUNC"));
                mdbLog.setAddr(fields.get("ADDR"));
                mdbLog.setNumber(fields.get("NUMBER"));
                mdbLog.setType(fields.get("type"));
                accessMdbLogsService.insertAccessMdbLogs(mdbLog);
            }
        }
    }

    @Scheduled(cron = "0 15 2 * * *")
    // @Scheduled(cron = "0/2 * * * * *")
    public void expired() {
        logger.info("清理过期数据");
        // LocalDateTime localDateTime = LocalDateTime.now().minusDays(183);
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(10);//fixme 发布时候修改
        accessMdbLogsMapper.deleteBefore(localDateTime);
    }

}
