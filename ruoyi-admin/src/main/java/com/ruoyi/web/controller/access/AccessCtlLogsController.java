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
import com.ruoyi.access.mapper.AccessCtlLogsMapper;
import com.ruoyi.access.service.IAccessCtlLogsService;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
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
 * 访问控制日志Controller
 * 
 * @author ruoyi
 * @date 2025-04-15
 */
@RestController
@RequestMapping("/access/logs")
public class AccessCtlLogsController extends BaseController
{
    @Autowired
    private IAccessCtlLogsService accessCtlLogsService;

    /**
     * 查询访问控制日志列表
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
     * 导出访问控制日志列表
     */
    @PreAuthorize("@ss.hasPermi('access:logs:export')")
    @Log(title = "访问控制日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccessCtlLogs accessCtlLogs)
    {
        List<AccessCtlLogs> list = accessCtlLogsService.selectAccessCtlLogsList(accessCtlLogs);
        ExcelUtil<AccessCtlLogs> util = new ExcelUtil<AccessCtlLogs>(AccessCtlLogs.class);
        util.exportExcel(response, list, "访问控制日志数据");
    }

    /**
     * 获取访问控制日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('access:logs:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(accessCtlLogsService.selectAccessCtlLogsById(id));
    }

    /**
     * 新增访问控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:add')")
    @Log(title = "访问控制日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccessCtlLogs accessCtlLogs)
    {
        return toAjax(accessCtlLogsService.insertAccessCtlLogs(accessCtlLogs));
    }

    /**
     * 修改访问控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:edit')")
    @Log(title = "访问控制日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccessCtlLogs accessCtlLogs)
    {
        return toAjax(accessCtlLogsService.updateAccessCtlLogs(accessCtlLogs));
    }

    /**
     * 删除访问控制日志
     */
    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "访问控制日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(accessCtlLogsService.deleteAccessCtlLogsByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('access:logs:remove')")
    @Log(title = "清空访问控制日志", businessType = BusinessType.DELETE)
    @PostMapping("/clear")
    public AjaxResult clear()
    {
        accessCtlLogsMapper.clear();
        return AjaxResult.success();
    }

    @Autowired
    ISysDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('access:logs:update')")
    @Log(title = "修改日志阈值", businessType = BusinessType.DELETE)
    @PostMapping("/threshold")
    public AjaxResult updateThreshold(@RequestBody SysDictData sysDictData) {
        return AjaxResult.success(dictDataService.updateDictData(sysDictData));
    }

    @Autowired AccessCtlLogsMapper accessCtlLogsMapper;
    @Scheduled(cron = "0 */1 * * * *")
    public void collect() {
        File file = new File("/var/access/acc_ctl.log");

        if (!file.exists()) return;

        int currentYear = Year.now().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMM dd HH:mm:ss", Locale.ENGLISH);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AccessCtlLogs ctlLog = new AccessCtlLogs();
                // 正则提取前面的时间戳、主机名和模块
                Pattern pattern = Pattern.compile("^(\\w+\\s+\\d+\\s+\\d+:\\d+:\\d+)\\s+(\\S+)\\s+(\\S+):\\s+\\[(\\w+)]\\s+(.*)$");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    ctlLog.setTs(LocalDateTime.parse(currentYear + " " + matcher.group(1), formatter));
                    ctlLog.setHost(matcher.group(2));
                    ctlLog.setModule(matcher.group(3));
                    ctlLog.setAction(matcher.group(4));

                    // 解析 key=value 部分
                    String keyValuePart = matcher.group(5);

                    Map<String, String> fields = new LinkedHashMap<>();

                    Matcher kvMatcher = Pattern.compile("(\\w+)=([^\\s]*)").matcher(keyValuePart);
                    while (kvMatcher.find()) {
                        fields.put(kvMatcher.group(1), kvMatcher.group(2));
                    }
                    ctlLog.setMac(fields.get("MAC"));
                    ctlLog.setSrc(fields.get("SRC"));
                    ctlLog.setDst(fields.get("DST"));
                    ctlLog.setSpt(fields.get("SPT"));
                    ctlLog.setDpt(fields.get("DPT"));
                    accessCtlLogsService.insertAccessCtlLogs(ctlLog);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 15 2 * * *")
    // @Scheduled(cron = "0/2 * * * * *")
    public void expired() {
        logger.info("清理过期数据");
        // LocalDateTime localDateTime = LocalDateTime.now().minusDays(183);//fixme 发布时候修改
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(10);//fixme 发布时候修改
        accessCtlLogsMapper.deleteBefore(localDateTime);
    }
}
