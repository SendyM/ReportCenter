package com.reportcenter.project.monitor.job.controller;

import com.reportcenter.framework.aspectj.lang.annotation.Log;
import com.reportcenter.framework.web.controller.BaseController;
import com.reportcenter.framework.web.domain.Message;
import com.reportcenter.framework.web.page.TableDataInfo;
import com.reportcenter.project.monitor.job.domain.Job;
import com.reportcenter.project.monitor.job.service.IJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 * 
 * @author Sendy
 */
@Controller
@RequestMapping("/monitor/job")
public class JobController extends BaseController
{
    private String prefix = "monitor/job";

    @Autowired
    private IJobService jobService;

    @RequiresPermissions("monitor:job:view")
    @GetMapping()
    public String job()
    {
        return prefix + "/job";
    }

    @RequiresPermissions("monitor:job:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(Job job)
    {
        setPageInfo(job);
        List<Job> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @Log(title = "监控管理", action = "定时任务-删除调度")
    @RequiresPermissions("monitor:job:remove")
    @RequestMapping("/remove/{jobId}")
    @ResponseBody
    public Message remove(@PathVariable("jobId") Long jobId)
    {
        Job job = jobService.selectJobById(jobId);
        if (job == null)
        {
            return Message.error("调度任务不存在");
        }
        if (jobService.deleteJob(job) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    @Log(title = "监控管理", action = "定时任务-批量删除")
    @RequiresPermissions("monitor:job:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public Message batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        try
        {
            jobService.batchDeleteJob(ids);
            return Message.ok();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
    }

    /**
     * 任务调度状态修改
     */
    @Log(title = "监控管理", action = "定时任务-状态修改")
    @RequiresPermissions("monitor:job:changeStatus")
    @PostMapping("/changeStatus")
    @ResponseBody
    public Message changeStatus(Job job)
    {
        if (jobService.changeStatus(job) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    /**
     * 新增调度
     */
    @Log(title = "监控管理", action = "定时任务-新增调度")
    @RequiresPermissions("monitor:job:add")
    @GetMapping("/add")
    public String add(Model model)
    {
        return prefix + "/add";
    }

    /**
     * 修改调度
     */
    @Log(title = "监控管理", action = "定时任务-修改调度")
    @RequiresPermissions("monitor:job:edit")
    @GetMapping("/edit/{jobId}")
    public String edit(@PathVariable("jobId") Long jobId, Model model)
    {
        Job job = jobService.selectJobById(jobId);
        model.addAttribute("job", job);
        return prefix + "/edit";
    }

    /**
     * 保存调度
     */
    @Log(title = "监控管理", action = "定时任务-保存调度")
    @RequiresPermissions("monitor:job:save")
    @PostMapping("/save")
    @ResponseBody
    public Message save(Job job)
    {
        if (jobService.saveJobCron(job) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }
}
