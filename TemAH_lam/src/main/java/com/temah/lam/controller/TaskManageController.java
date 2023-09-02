package com.temah.lam.controller;

import com.temah.common.page.PagingResult;
import com.temah.common.web.BaseController;
import com.temah.common.web.BaseService;
import com.temah.common.web.RestResult;
import com.temah.lam.model.ScheduleSetting;
import com.temah.lam.scheduling.CronTaskRegistrar;
import com.temah.lam.scheduling.SchedulingRunnable;
import com.temah.lam.service.ScheduleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskManageController extends BaseController<ScheduleSetting, Integer> {
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    ScheduleSettingService scheduleSettingService;

    public TaskManageController(ScheduleSettingService service) {
        super(service);
        this.scheduleSettingService = service;
    }

    /**
     * 添加定时任务
     */
    @PostMapping("add")
    public RestResult add(@RequestBody ScheduleSetting sysJob) {
        sysJob.setCreateTime(new Date());
        sysJob.setUpdateTime(new Date());

        int affectedRows = scheduleSettingService.create(sysJob);

        if (affectedRows > 0 && sysJob.getJobStatus().equals(1)) {
            // 添加成功,并且状态是1，直接放入任务器
            SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
            cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
        }
        return RestResult.success(affectedRows);
    }

    /**
     * 修改定时任务
     */
    @PostMapping("update")
    public RestResult update(@RequestBody ScheduleSetting sysJob) throws Exception {
        // 查询修改前任务
        ScheduleSetting existedSysJob = scheduleSettingService.findById(sysJob.getJobId());
        int affectedRows = 0;
        if (existedSysJob != null) {
            // 修改任务
            sysJob.setUpdateTime(new Date());
            sysJob.setCreateTime(existedSysJob.getCreateTime());
            affectedRows = scheduleSettingService.update(sysJob);

            // 修改成功,则先删除任务器中的任务,并重新添加
            SchedulingRunnable task1 = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
            cronTaskRegistrar.removeCronTask(task1);
            if (sysJob.getJobStatus().equals(1)) {
                // 如果修改后的任务状态是1就加入任务器
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
            }
        }
        return RestResult.success(affectedRows);
    }

    /**
     * 删除任务
     */
    @PostMapping("del/{jobId}")
    public RestResult del(@PathVariable("jobId") Integer jobId) {
        // 先查询要删除的任务信息
        ScheduleSetting existedSysJob = scheduleSettingService.findById(jobId);
        int affectedRows = 0;
        if (existedSysJob != null) {
            // 删除
            affectedRows = scheduleSettingService.delete(jobId);
            if (affectedRows > 0) {
                // 删除成功时要清除定时任务器中的对应任务
                SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
                cronTaskRegistrar.removeCronTask(task);
            }
        }
        return RestResult.success(affectedRows);
    }

    // 停止/启动任务
    @PostMapping("changesStatus/{jobId}/{stop}")
    public RestResult changesStatus(@PathVariable("jobId") Integer jobId, @PathVariable("stop") Integer stop) throws Exception {
        // 修改任务状态
        ScheduleSetting existedSysJob = scheduleSettingService.findById(jobId);
        int affectedRows = 0;
        if (existedSysJob != null) {
            existedSysJob.setJobStatus(stop);
            existedSysJob.setUpdateTime(new Date());
            affectedRows = scheduleSettingService.update(existedSysJob);

            // 如果状态是1则添加任务
            if (existedSysJob.getJobStatus().equals(1)) {
                SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, existedSysJob.getCronExpression());
            } else {
                // 否则清除任务
                SchedulingRunnable task = new SchedulingRunnable(existedSysJob.getBeanName(), existedSysJob.getMethodName(), existedSysJob.getMethodParams());
                cronTaskRegistrar.removeCronTask(task);
            }
        }
        return RestResult.success(affectedRows);
    }

    @GetMapping("/get/{id}")
    public RestResult get(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("缺少必填值");
        }
        return RestResult.success(service.findById(id));
    }

    @GetMapping("/getAll")
    public RestResult findWith(@RequestParam(name = "sortBy", required = false) String sortBy,
                               @RequestParam(name = "sortOrder", required = false) String sortOrder,
                               @RequestParam(name = "pageIndex", required = false) Integer pageIndex,
                               @RequestParam(name = "pageSize", required = false) Integer pageSize,
                               HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        setPageWithSort(params, sortBy, sortOrder, pageIndex, pageSize, request);
        PagingResult pr = service.findByCriteria(params);
        return RestResult.success(pr);
    }
}
