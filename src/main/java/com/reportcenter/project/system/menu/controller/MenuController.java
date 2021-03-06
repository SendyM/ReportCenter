package com.reportcenter.project.system.menu.controller;

import com.reportcenter.framework.aspectj.lang.annotation.Log;
import com.reportcenter.framework.web.controller.BaseController;
import com.reportcenter.framework.web.domain.Message;
import com.reportcenter.project.system.menu.domain.Menu;
import com.reportcenter.project.system.menu.service.IMenuService;
import com.reportcenter.project.system.role.domain.Role;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 * 
 * @author Sendy
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends BaseController
{

    private String prefix = "system/menu";

    @Autowired
    private IMenuService menuService;

    @RequiresPermissions("system:menu:view")
    @GetMapping()
    public String menu()
    {
        return prefix + "/menu";
    }

    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    @ResponseBody
    public List<Menu> list()
    {
        List<Menu> menuList = menuService.selectMenuAll();
        return menuList;
    }

    /**
     * 删除菜单
     */
    @Log(title = "系统管理", action = "菜单管理-删除菜单")
    @RequiresPermissions("system:menu:remove")
    @GetMapping("/remove/{menuId}")
    @ResponseBody
    public Message remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.deleteMenuById(menuId) > 0)
        {
            return Message.ok();
        }
        else
        {
            return Message.error(1, "删除失败");
        }
    }

    /**
     * 修改菜单
     */
    @Log(title = "系统管理", action = "菜单管理-修改菜单")
    @RequiresPermissions("system:menu:edit")
    @GetMapping("/edit/{menuId}")
    public String edit(@PathVariable("menuId") Long menuId, Model model)
    {
        Menu menu = menuService.selectMenuById(menuId);
        model.addAttribute("menu", menu);
        return prefix + "/edit";
    }

    /**
     * 新增
     */
    @Log(title = "系统管理", action = "菜单管理-新增菜单")
    @RequiresPermissions("system:menu:add")
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, Model model)
    {
        Menu menu = null;
        if (0L != parentId)
        {
            menu = menuService.selectMenuById(parentId);
        }
        else
        {
            menu = new Menu();
            menu.setMenuId(0L);
            menu.setMenuName("主目录");
        }
        model.addAttribute("menu", menu);
        return prefix + "/add";
    }

    /**
     * 保存菜单
     */
    @Log(title = "系统管理", action = "菜单管理-保存菜单")
    @RequiresPermissions("system:menu:save")
    @PostMapping("/save")
    @ResponseBody
    public Message save(Menu menu)
    {
        if (menuService.saveMenu(menu) > 0)
        {
            return Message.ok();
        }
        return Message.error();
    }

    /**
     * 选择菜单图标
     */
    @GetMapping("/icon")
    public String icon()
    {
        return prefix + "/icon";
    }

    /**
     * 校验菜单名称
     */
    @PostMapping("/checkMenuNameUnique")
    @ResponseBody
    public String checkMenuNameUnique(Menu menu)
    {
        String uniqueFlag = "0";
        if (menu != null)
        {
            uniqueFlag = menuService.checkMenuNameUnique(menu);
        }
        return uniqueFlag;
    }

    /**
     * 加载角色菜单列表树
     */
    @GetMapping("/roleMenuTreeData")
    @ResponseBody
    public List<Map<String, Object>> roleMenuTreeData(Role role)
    {
        List<Map<String, Object>> tree = menuService.roleMenuTreeData(role);
        return tree;
    }
    
    /**
     * 加载所有菜单列表树
     */
    @GetMapping("/menuTreeData")
    @ResponseBody
    public List<Map<String, Object>> menuTreeData(Role role)
    {
        List<Map<String, Object>> tree = menuService.menuTreeData();
        return tree;
    }
    
    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree/{menuId}")
    public String selectMenuTree(@PathVariable("menuId") Long  menuId, Model model)
    {
        model.addAttribute("treeName", menuService.selectMenuById(menuId).getMenuName());
        return prefix + "/tree";
    }
}