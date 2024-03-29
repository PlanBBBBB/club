package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.*;
import com.java.dto.MemberAddDto;
import com.java.entity.*;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("membersService")
public class MembersService {

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


    @Transactional(rollbackFor = Exception.class)
    public void add(MemberAddDto memberAddDto) {
        //加入社团表
        Members members = new Members();
        BeanUtils.copyProperties(memberAddDto, members);
        members.setId(IDUtils.makeIDByCurrent());
        membersDao.insert(members);
        //修改申请表
        LambdaQueryWrapper<ApplyLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplyLogs::getUserId, memberAddDto.getUserId())
                .eq(ApplyLogs::getTeamId, memberAddDto.getTeamId());
        ApplyLogs applyLogs = applyLogsDao.selectOne(queryWrapper);
        applyLogs.setStatus(1);
        applyLogsDao.updateById(applyLogs);
    }


    public void update(Members members) {
        membersDao.updateById(members);
    }


    @Transactional
    public void delete(Members members) {

        LambdaQueryWrapper<PayLogs> qw_pay = new LambdaQueryWrapper<>();
        qw_pay.eq(PayLogs::getUserId, members.getUserId());
        payLogsDao.delete(qw_pay);

        LambdaQueryWrapper<ActiveLogs> qw_active = new LambdaQueryWrapper<>();
        qw_active.eq(ActiveLogs::getUserId, members.getUserId());
        activeLogsDao.delete(qw_active);

        LambdaQueryWrapper<ApplyLogs> qw_apply = new LambdaQueryWrapper<>();
        qw_apply.eq(ApplyLogs::getUserId, members.getUserId());
        applyLogsDao.delete(qw_apply);

        membersDao.deleteById(members);

        Teams team = teamsDao.selectById(members.getTeamId());
        team.setTotal(team.getTotal() - 1);
        teamsDao.updateById(team);
    }


    public Members getOne(String id) {
        return membersDao.selectById(id);
    }


    public Boolean isManager(String teamId, String userId) {
        LambdaQueryWrapper<Teams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teams::getId, teamId).eq(Teams::getManager, userId);
        return teamsDao.selectCount(queryWrapper) > 0;
    }


    public PageData getPageAll(Long pageIndex, Long pageSize, String teamName, String userName) {
        Page<Map<String, Object>> page = membersDao.qryPageAll(new Page<>(pageIndex, pageSize), teamName, userName);
        return parsePage(page);
    }


    public PageData getPageByManId(Long pageIndex, Long pageSize, String manId, String teamName, String userName) {
        Page<Map<String, Object>> page = membersDao.qryPageByManId(new Page<>(pageIndex, pageSize), manId, teamName, userName);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());
    }

}