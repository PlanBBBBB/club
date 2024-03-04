package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.*;
import com.java.entity.*;
import com.java.service.MembersService;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("membersService")
public class MembersServiceImpl implements MembersService {

    @Resource
    private TeamsDao teamsDao;

    @Resource
    private MembersDao membersDao;

    @Resource
    private PayLogsDao payLogsDao;

    @Resource
    private ActiveLogsDao activeLogsDao;

    @Resource
    private ApplyLogsDao applyLogsDao;

    @Override
    public void add(Members members) {
        membersDao.insert(members);
    }

    @Override
    public void update(Members members) {
        membersDao.updateById(members);
    }

    @Override
    @Transactional
    public void delete(Members members) {

        QueryWrapper<PayLogs> qw_pay = new QueryWrapper<>();
        qw_pay.eq("user_id", members.getUserId());
        payLogsDao.delete(qw_pay);

        QueryWrapper<ActiveLogs> qw_active = new QueryWrapper<>();
        qw_active.eq("user_id", members.getUserId());
        activeLogsDao.delete(qw_active);

        QueryWrapper<ApplyLogs> qw_apply = new QueryWrapper<>();
        qw_apply.eq("user_id", members.getUserId());
        applyLogsDao.delete(qw_apply);

        membersDao.deleteById(members);

        Teams team = teamsDao.selectById(members.getTeamId());
        team.setTotal(team.getTotal() - 1);
        teamsDao.updateById(team);
    }

    @Override
    public Members getOne(String id) {
        return membersDao.selectById(id);
    }

    @Override
    public Boolean isManager(String teamId, String userId){
        QueryWrapper<Teams> qw = new QueryWrapper<>();
        qw.eq("manager", userId);
        qw.eq("id", teamId);
        return teamsDao.selectCount(qw) > 0;
    }

    @Override
    public PageData getPageAll(Long pageIndex, Long pageSize, String teamName, String userName) {
        Page<Map<String, Object>> page =
                membersDao.qryPageAll(new Page<>(pageIndex, pageSize), teamName, userName);
        return parsePage(page);
    }

    @Override
    public PageData getPageByManId(Long pageIndex, Long pageSize, String manId, String teamName, String userName) {
        Page<Map<String, Object>> page =
                membersDao.qryPageByManId(new Page<>(pageIndex, pageSize), manId, teamName, userName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }
}