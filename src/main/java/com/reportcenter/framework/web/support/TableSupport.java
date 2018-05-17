package com.reportcenter.framework.web.support;

import com.reportcenter.common.utils.MapDataUtil;
import com.reportcenter.common.utils.ServletUtils;
import com.reportcenter.framework.web.page.PageUtilEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 表格数据处理
 * 
 * @author Sendy
 */
public class TableSupport
{
    /**
     * 封装分页对象
     */
    public static PageUtilEntity getPageUtilEntity()
    {
        HttpServletRequest request = ServletUtils.getHttpServletRequest();
        PageUtilEntity pageUtilEntity = new PageUtilEntity();
        pageUtilEntity.setPage(Integer.valueOf(request.getParameter("offset")));
        pageUtilEntity.setSize(Integer.valueOf(request.getParameter("limit")));
        pageUtilEntity.setOrderByColumn(request.getParameter("sort"));
        pageUtilEntity.setIsAsc(request.getParameter("order"));
        pageUtilEntity.setSearchValue(request.getParameter("search"));
        pageUtilEntity.setReqMap(MapDataUtil.convertDataMap(request));
        return pageUtilEntity;
    }

    public static PageUtilEntity buildPageRequest()
    {
        return getPageUtilEntity();
    }

}
