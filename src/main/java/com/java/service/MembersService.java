package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.*;
import com.java.dto.MemberAddDto;
import com.java.entity.*;
import com.java.utils.IDUtils;
import com.java.vo.PageData;
import com.java.vo.R;
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

    @Resource
    private ApplyDao applyDao;


    public void add(MemberAddDto memberAddDto) {
        Members members = new Members();
        BeanUtils.copyProperties(memberAddDto, members);
        members.setId(IDUtils.makeIDByCurrent());
        membersDao.insert(members);
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

    public R apply(MemberAddDto memberAddDto) {
        Teams team = teamsDao.selectById(memberAddDto.getTeamId());
        if (team == null) {
            return R.error("团队不存在");
        }
        if (team.getManager().equals(memberAddDto.getUserId())){
            return R.error("社长不能申请加入自己的团队");
        }
        Integer i = membersDao.selectCount(new LambdaQueryWrapper<Members>().eq(Members::getUserId, memberAddDto.getUserId())
                .eq(Members::getTeamId, memberAddDto.getTeamId()));
        if (i > 0) {
            return R.error("您已经是团队成员了");
        }
        Integer i1 = applyDao.selectCount(new LambdaQueryWrapper<Apply>().eq(Apply::getUserId, memberAddDto.getUserId())
                .eq(Apply::getTeamId, memberAddDto.getTeamId()));
        if (i1 > 0) {
            return R.error("您已经申请过了");
        }
        Apply apply = new Apply();
        BeanUtils.copyProperties(memberAddDto, apply);
        apply.setSuccess("0");
        apply.setId(IDUtils.makeIDByCurrent());
        applyDao.insert(apply);
        return R.success();
    }
}