package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.ApplyLogsDao;
import com.java.dao.MembersDao;
import com.java.dao.TeamsDao;
import com.java.entity.ApplyLogs;
import com.java.entity.Members;
import com.java.entity.Teams;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("applyLogsService")
public class ApplyLogsService {

    @Resource
    private MembersDao membersDao;

    @Resource
    private ApplyLogsDao applyLogsDao;

    @Resource
    private TeamsDao teamsDao;

    
    public void add(ApplyLogs applyLogs) {
        applyLogsDao.insert(applyLogs);
    }

    
    @Transactional
    public void update(ApplyLogs applyLogs) {
        if(applyLogs.getStatus() != null && applyLogs.getStatus() == 1){
            Members member = new Members();
            member.setId(IDUtils.makeIDByCurrent());
            member.setCreateTime(DateUtils.getNowDate());
            member.setUserId(applyLogs.getUserId());
            member.setTeamId(applyLogs.getTeamId());
            membersDao.insert(member);
            Teams teams = teamsDao.selectById(applyLogs.getTeamId());
            teams.setTotal(teams.getTotal() + 1);
            teamsDao.updateById(teams);
        }
        applyLogsDao.updateById(applyLogs);
    }

    
    public void delete(ApplyLogs applyLogs) {
        applyLogsDao.deleteById(applyLogs);
    }

    
    public Boolean isApply(String userId, String teamId){
        LambdaQueryWrapper<ApplyLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplyLogs::getUserId, userId)
                .eq(ApplyLogs::getTeamId, teamId)
                .eq(ApplyLogs::getStatus, 0);
        return applyLogsDao.selectCount(queryWrapper) <= 0;
    }

    
    public ApplyLogs getOne(String id) {
        return applyLogsDao.selectById(id);
    }

    
    public PageData getManPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {
        Page<Map<String, Object>> page =
                applyLogsDao.qryManPageInfo(new Page<>(pageIndex, pageSize), userId, teamName, userName);
        return parsePage(page);
    }

    
    public PageData getPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {
        Page<Map<String, Object>> page =
                applyLogsDao.qryPageInfo(new Page<>(pageIndex, pageSize), userId, teamName, userName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}