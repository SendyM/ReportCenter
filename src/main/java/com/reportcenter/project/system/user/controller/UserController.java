package com.reportcenter.project.system.user.controller;

import com.reportcenter.framework.aspectj.lang.annotation.Log;
import com.reportcenter.framework.web.controller.BaseController;
import com.reportcenter.framework.web.domain.Message;
import com.reportcenter.framework.web.page.TableDataInfo;
import com.reportcenter.project.system.post.domain.Post;
import com.reportcenter.project.system.post.service.IPostService;
import com.reportcenter.project.system.role.domain.Role;
import com.reportcenter.project.system.role.service.IRoleService;
import com.reportcenter.project.system.user.domain.User;
import com.reportcenter.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息
 * 
 * @author Sendy
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController
{

    private String prefix = "system/user";

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IRoleService roleService;
    
    @Autowired
    private IPostService postService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(User user)
    {
        setPageInfo(user);
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 修改用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "系统管理", action = "用户管理-修改用户")
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, Model model)
    {
        User user = userService.selectUserById(userId);
        List<Role> roles = roleService.selectRolesByUserId(userId);
        List<Post> posts = postService.selectPostsByUserId(userId);
        model.addAttribute("roles", roles);
        model.addAttribute("posts", posts);
        model.addAttribute("user", user);
        return prefix + "/edit";
    }

    /**
     * 新增用户
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "系统管理", action = "用户管理-新增用户")
    @GetMapping("/add")
    public String add(Model model)
    {
        List<Role> roles = roleService.selectRoleAll();
        List<Post> posts = postService.selectPostAll();
        model.addAttribute("roles", roles);
        model.addAttribute("posts", posts);
        return prefix + "/add";
    }
    
    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "系统管理", action = "用户管理-重置密码")
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, Model model)
    {
        User user = userService.selectUserById(userId);
        model.addAttribute("user", user);
        return prefix + "/resetPwd";
    }
    
    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "系统管理", action = "用户管理-重置密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    public Message resetPwd(User user)
    {
        int rows = userService.updateUser(user);
        if (rows > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "系统管理", action = "用户管理-删除用户")
    @RequestMapping("/remove/{userId}")
    @ResponseBody
    public Message remove(@PathVariable("userId") Long userId)
    {
        User user = userService.selectUserById(userId);
        if (user == null)
        {
            return Message.error("用户不存在");
        }
        if (userService.deleteUserById(userId) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    @RequiresPermissions("system:user:batchRemove")
    @Log(title = "系统管理", action = "用户管理-批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    public Message batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int rows = userService.batchDeleteUser(ids);
        if (rows > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    /**
     * 保存
     */
    @RequiresPermissions("system:user:save")
    @Log(title = "系统管理", action = "部门管理-保存部门")
    @PostMapping("/save")
    @ResponseBody
    public Message save(User user)
    {
        if (userService.saveUser(user) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkUserNameUnique")
    @ResponseBody
    public String checkUserNameUnique(User user)
    {
        String uniqueFlag = "0";
        if (user != null)
        {
            uniqueFlag = userService.checkUserNameUnique(user.getLoginName());
        }
        return uniqueFlag;
    }

}