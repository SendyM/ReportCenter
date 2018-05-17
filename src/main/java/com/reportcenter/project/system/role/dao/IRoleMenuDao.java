package com.reportcenter.project.system.role.dao;

import com.reportcenter.project.system.role.domain.RoleMenu;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 * 
 * @author Sendy
 */
public interface IRoleMenuDao
{

    /**
     * 通过角色ID删除角色和菜单关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(Long roleId);
    
    /**
     * 批量新增角色菜单信息
     * 
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<RoleMenu> roleMenuList);

}
