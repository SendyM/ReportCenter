package com.reportcenter.project.monitor.logininfor.service;

import com.reportcenter.project.monitor.logininfor.dao.ILogininforDao;
import com.reportcenter.project.monitor.logininfor.domain.Logininfor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author Sendy
 */
@Service("logininforService")
public class LogininforServiceImpl implements ILogininforService
{

    @Autowired
    private ILogininforDao logininforDao;

    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor)
    {
        logininforDao.insertLogininfor(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor)
    {
        return logininforDao.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param ids 需要删除的数据
     * @return
     */
    @Override
    public int batchDeleteLogininfor(Long[] ids)
    {
        return logininforDao.batchDeleteLogininfor(ids);
    }
}
