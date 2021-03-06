package com.reportcenter.project.system.post.controller;

import com.reportcenter.framework.aspectj.lang.annotation.Log;
import com.reportcenter.framework.web.controller.BaseController;
import com.reportcenter.framework.web.domain.Message;
import com.reportcenter.framework.web.page.TableDataInfo;
import com.reportcenter.project.system.post.domain.Post;
import com.reportcenter.project.system.post.service.IPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 * 
 * @author Sendy
 */
@Controller
@RequestMapping("/system/post")
public class PostController extends BaseController
{
    private String prefix = "system/post";

    @Autowired
    private IPostService postService;

    @RequiresPermissions("system:post:view")
    @GetMapping()
    public String operlog()
    {
        return prefix + "/post";
    }

    @RequiresPermissions("system:post:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(Post post)
    {
        setPageInfo(post);
        List<Post> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    /**
     * 删除
     */
    @Log(title = "系统管理", action = "岗位管理-删除岗位")
    @RequiresPermissions("system:post:remove")
    @RequestMapping("/remove/{postId}")
    @ResponseBody
    public Message remove(@PathVariable("postId") Long postId)
    {
        Post post = postService.selectPostById(postId);
        if (post == null)
        {
            return Message.error("岗位不存在");
        }
        if (postService.deletePostById(postId) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    @RequiresPermissions("system:post:batchRemove")
    @Log(title = "系统管理", action = "岗位管理-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public Message batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = postService.batchDeletePost(ids);
        if (rows > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    /**
     * 新增岗位
     */
    @Log(title = "系统管理", action = "岗位管理-新增岗位")
    @RequiresPermissions("system:post:add")
    @GetMapping("/add")
    public String add(Model model)
    {
        return prefix + "/add";
    }

    /**
     * 修改岗位
     */
    @Log(title = "系统管理", action = "岗位管理-修改岗位")
    @RequiresPermissions("system:post:edit")
    @GetMapping("/edit/{postId}")
    public String edit(@PathVariable("postId") Long postId, Model model)
    {
        Post post = postService.selectPostById(postId);
        model.addAttribute("post", post);
        return prefix + "/edit";
    }

    /**
     * 保存岗位
     */
    @Log(title = "系统管理", action = "岗位管理-保存岗位")
    @RequiresPermissions("system:post:save")
    @PostMapping("/save")
    @ResponseBody
    public Message save(Post post)
    {
        if (postService.savePost(post) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

}
