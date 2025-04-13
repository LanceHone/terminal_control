package com.ruoyi.web.controller.access;

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
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.access.domain.AccessCtlLogs;
import com.ruoyi.access.mapper.AccessCtlLogsMapper;
import com.ruoyi.access.mapper.AccessMdbLogsMapper;
import com.ruoyi.access.service.IAccessCtlLogsService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.mapper.SysLogininforMapper;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 控制日志Controller
 * 
 * @author ruoyi
 * @date 2025-04-13
 */
@RestController
@RequestMapping("/access/logs")
public class AccessCtlLogsController extends BaseController
{
    @Autowired
    private IAccessCtlLogsService accessCtlLogsService;

    /**
     * 查询控制日志列表
     */
    @PreAuthorize("@ss.hasPermi('access:logs:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccessCtlLogs accessCtlLogs)
    {
        startPage();
        List<AccessCtlLogs> list = accessCtlLogsService.selectAccessCtlLogsList(accessCtlLogs);
        return getDataTable(list);
    }

    /**
     * 导出控制日志列表
     */
    @PreAuthorize("@ss.hasPermi('access:logs:export')")
    @Log(title = "控制日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccessCtlLogs accessCtlLogs)
    {
        List<AccessCtlLogs> list = accessCtlLogsService.selectAccessCtlLogsList(accessCtlLogs);
        ExcelUtil<AccessCtlLogs> util = new ExcelUtil<AccessCtlLogs>(AccessCtlLogs.class);
        util.exportExcel(response, list, "控制日志数据");
    }

    /**
     * 获取控制日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('access:logs:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(accessCtlLogsService.selectAccessCtlLogsById(id));
    }

    /**
     * 新增控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:add')")
    @Log(title = "控制日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccessCtlLogs accessCtlLogs)
    {
        return toAjax(accessCtlLogsService.insertAccessCtlLogs(accessCtlLogs));
    }

    /**
     * 修改控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:edit')")
    @Log(title = "控制日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccessCtlLogs accessCtlLogs)
    {
        return toAjax(accessCtlLogsService.updateAccessCtlLogs(accessCtlLogs));
    }

    /**
     * 删除控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "控制日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accessCtlLogsService.deleteAccessCtlLogsByIds(ids));
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void parseLogFile() {
        String filePath = "/var/access/acc_ctl.log";
        Path path = Paths.get(filePath);
        List<AccessCtlLogs> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AccessCtlLogs entry = parseLogLine(line);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // 清空文件内容
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        entries.forEach(entry -> accessCtlLogsService.insertAccessCtlLogs(entry));
    }

    @Autowired
    AccessCtlLogsMapper accessMdbLogsMapper;
    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "清空访问控制日志", businessType = BusinessType.DELETE)
    @PostMapping("/clear")
    public AjaxResult clear(@PathVariable Long[] ids)
    {
        accessMdbLogsMapper.clear();
        return AjaxResult.success();
    }

    @Autowired ISysDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('access:logs:update')")
    @Log(title = "修改日志阈值", businessType = BusinessType.DELETE)
    @PostMapping("/threshold")
    public AjaxResult updateThreshold(@RequestBody SysDictData sysDictData) {
        return AjaxResult.success(dictDataService.updateDictData(sysDictData));
    }

    private AccessCtlLogs parseLogLine(String logLine) {
        if (logLine == null || logLine.isEmpty()) return null;

        String timestamp = "";
        String bracketContent = "";
        String src = null, dst = null, spt = null, dpt = null;

        // 时间戳
        Matcher timeMatcher = Pattern.compile("^(\\S+)").matcher(logLine);
        if (timeMatcher.find()) {
            timestamp = timeMatcher.group(1);
        }

        // 中括号内容
        Matcher bracketMatcher = Pattern.compile("\\[(.*?)\\]").matcher(logLine);
        if (bracketMatcher.find()) {
            bracketContent = bracketMatcher.group(1);
        }

        // 动态字段提取
        Map<String, String> fieldMap = new HashMap<>();
        String[] keys = {"SRC", "DST", "SPT", "DPT"};
        for (String key : keys) {
            Matcher m = Pattern.compile(key + "=(\\S+)").matcher(logLine);
            if (m.find()) {
                fieldMap.put(key, m.group(1));
            }
        }

        src = fieldMap.getOrDefault("SRC", null);
        dst = fieldMap.getOrDefault("DST", null);
        spt = fieldMap.getOrDefault("SPT", null);
        dpt = fieldMap.getOrDefault("DPT", null);

        return new AccessCtlLogs(timestamp, bracketContent, src, dst, spt, dpt);
    }
}
