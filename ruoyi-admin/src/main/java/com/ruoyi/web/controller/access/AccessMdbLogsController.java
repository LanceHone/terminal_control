package com.ruoyi.web.controller.access;

import com.ruoyi.access.domain.AccessMdbLogs;
import com.ruoyi.access.mapper.AccessMdbLogsMapper;
import com.ruoyi.access.service.IAccessMdbLogsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * modbus控制日志Controller
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
@RestController
@RequestMapping("/access/mdb/logs")
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
    public AjaxResult clear(@PathVariable Long[] ids)
    {
        accessMdbLogsMapper.clear();
        return AjaxResult.success();
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void parseLogFile() {
        System.out.println("开始收集日志文件");
        String filePath = "/var/access/mdb.log";
        Path path = Paths.get(filePath);
        List<AccessMdbLogs> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AccessMdbLogs entry = parseLogLine(line);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // 清空文件内容
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            // 写入空字符串即可清空
            writer.write("");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        entries.forEach(entry -> accessMdbLogsService.insertAccessMdbLogs(entry));
    }

    @Autowired
    private ISysDictTypeService dictTypeService;

    private AccessMdbLogs parseLogLine(String logLine) {
        if (logLine == null || logLine.isEmpty()) return null;

        String timestamp = "";
        String bracketContent = "";
        String u = null, f = null, addr = null, num = null;

        // 时间戳
        Matcher timeMatcher = Pattern.compile("^(\\S+)").matcher(logLine);
        if (timeMatcher.find()) {
            timestamp = timeMatcher.group(1);
        }

        // 动态字段提取
        Map<String, String> fieldMap = new HashMap<>();
        String[] keys = {"uid", "fid", "addr", "number"};
        for (String key : keys) {
            Matcher m = Pattern.compile(key + "=(\\S+)").matcher(logLine);
            if (m.find()) {
                fieldMap.put(key, m.group(1));
            }
        }

        u = fieldMap.getOrDefault("uid", null);
        f = fieldMap.getOrDefault("fid", null);
        addr = fieldMap.getOrDefault("addr", null);
        num = fieldMap.getOrDefault("number", null);

        return new AccessMdbLogs(timestamp,u, f, addr, num);
    }
}
