package com.reportcenter.project.monitor.online.controller;

import com.reportcenter.framework.aspectj.lang.annotation.Log;
import com.reportcenter.framework.shiro.session.OnlineSessionDAO;
import com.reportcenter.framework.web.controller.BaseController;
import com.reportcenter.framework.web.domain.Message;
import com.reportcenter.framework.web.page.TableDataInfo;
import com.reportcenter.project.monitor.online.domain.OnlineSession;
import com.reportcenter.project.monitor.online.domain.UserOnline;
import com.reportcenter.project.monitor.online.service.IUserOnlineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户监控
 * 
 * @author Sendy
 */
@Controller
@RequestMapping("/monitor/online")
public class UserOnlineController extends BaseController
{
    private String prefix = "monitor/online";

    @Autowired
    private IUserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online()
    {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserOnline userOnline)
    {
        setPageInfo(userOnline);
        List<UserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:online:batchForceLogout")
    @Log(title = "监控管理", action = "在线用户-批量强退用户")
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public Message batchForceLogout(@RequestParam("ids[]") String[] ids)
    {
        for (String sessionId : ids)
        {
            UserOnline online = userOnlineService.selectOnlineById(sessionId);
            if (online == null)
            {
                return Message.error("用户已下线");
            }
            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
            if (onlineSession == null)
            {
                return Message.error("用户已下线");
            }
            onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
            online.setStatus(OnlineSession.OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
        }
        return Message.ok();
    }

    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "监控管理", action = "在线用户-强退用户")
    @RequestMapping("/forceLogout/{sessionId}")
    @ResponseBody
    public Message forceLogout(@PathVariable("sessionId") String sessionId)
    {
        UserOnline online = userOnlineService.selectOnlineById(sessionId);
        if (online == null)
        {
            return Message.error("用户已下线");
        }
        OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
        if (onlineSession == null)
        {
            return Message.error("用户已下线");
        }
        onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
        online.setStatus(OnlineSession.OnlineStatus.off_line);
        userOnlineService.saveOnline(online);
        return Message.ok();
    }

}
