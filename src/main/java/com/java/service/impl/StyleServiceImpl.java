package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.ActiveLogsDao;
import com.java.dao.StyleDao;
import com.java.dao.TeamsDao;
import com.java.entity.ActiveLogs;
import com.java.entity.Style;
import com.java.entity.Teams;
import com.java.service.StyleService;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("styleService")
public class StyleServiceImpl implements StyleService {

    @Resource
    private TeamsDao teamsDao;

    @Resource
    private ActiveLogsDao activeLogsDao;

    @Resource
    private StyleDao styleDao;

    @Override
    @Transactional
    public void add(Style style) {
        styleDao.insert(style);
        Teams teams = teamsDao.selectById(style.getTeamId());
        ActiveLogs activeLog = new ActiveLogs();
        activeLog.setId(IDUtils.makeIDByCurrent());
        activeLog.setActiveId(style.getId());
        activeLog.setUserId(teams.getManager());
        activeLog.setCreateTime(DateUtils.getNowDate());
        activeLogsDao.insert(activeLog);
    }

    @Override
    public void update(Style style) {
        styleDao.updateById(style);
    }

    @Override
    @Transactional
    public void delete(Style style) {
        LambdaQueryWrapper<ActiveLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActiveLogs::getActiveId, style.getId());
        activeLogsDao.delete(queryWrapper);
        styleDao.deleteById(style);
    }

    @Override
    public Style getOne(String id) {
        return styleDao.selectById(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String activeName, String teamName) {
        Page<Map<String, Object>> page =
                styleDao.qryPageAll(new Page<>(pageIndex, pageSize), activeName, teamName);
        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName) {
        Page<Map<String, Object>> page =
                styleDao.qryPageByMemId(new Page<>(pageIndex, pageSize), userId, activeName, teamName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}