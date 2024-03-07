package com.java.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.PayLogsDao;
import com.java.entity.PayLogs;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("payLogsService")
public class PayLogsService {

    @Resource
    private PayLogsDao payLogsDao;

    
    public void add(PayLogs payLogs) {
        payLogsDao.insert(payLogs);
    }

    
    public void update(PayLogs payLogs) {
        payLogsDao.updateById(payLogs);
    }

    
    public void delete(PayLogs payLogs) {
        payLogsDao.deleteById(payLogs);
    }

    
    public PayLogs getOne(String id) {
        return payLogsDao.selectById(id);
    }

    
    public PageData getManPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {
        Page<Map<String, Object>> page = payLogsDao.qryManPageInfo(new Page<>(pageIndex, pageSize), userId, teamName, userName);
        return parsePage(page);
    }

    
    public PageData getPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {
        Page<Map<String, Object>> page = payLogsDao.qryPageInfo(new Page<>(pageIndex, pageSize), userId, teamName, userName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}