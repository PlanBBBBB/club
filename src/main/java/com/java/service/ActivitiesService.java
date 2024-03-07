package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.ActiveLogsDao;
import com.java.dao.ActivitiesDao;
import com.java.dao.TeamsDao;
import com.java.entity.ActiveLogs;
import com.java.entity.Activities;
import com.java.entity.Teams;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("activitiesService")
public class ActivitiesService {

    @Resource
    private TeamsDao teamsDao;

    @Resource
    private ActiveLogsDao activeLogsDao;

    @Resource
    private ActivitiesDao activitiesDao;

    
    @Transactional
    public void add(Activities activities) {
        activitiesDao.insert(activities);
        Teams teams = teamsDao.selectById(activities.getTeamId());
        ActiveLogs activeLog = new ActiveLogs();
        activeLog.setId(IDUtils.makeIDByCurrent());
        activeLog.setActiveId(activities.getId());
        activeLog.setUserId(teams.getManager());
        activeLog.setCreateTime(DateUtils.getNowDate());
        activeLogsDao.insert(activeLog);
    }

    
    public void update(Activities activities) {
        activitiesDao.updateById(activities);
    }

    
    @Transactional
    public void delete(Activities activities) {
        LambdaQueryWrapper<ActiveLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActiveLogs::getActiveId, activities.getId());
        activeLogsDao.delete(queryWrapper);
        activitiesDao.deleteById(activities);
    }

    
    public Activities getOne(String id) {
        return activitiesDao.selectById(id);
    }

    
    public PageData getPageAll(Long pageIndex, Long pageSize, String activeName, String teamName) {
        Page<Map<String, Object>> page = activitiesDao.qryPageAll(new Page<>(pageIndex, pageSize), activeName, teamName);
        return parsePage(page);
    }

    
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName) {
        Page<Map<String, Object>> page = activitiesDao.qryPageByMemId(new Page<>(pageIndex, pageSize), userId, activeName, teamName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}