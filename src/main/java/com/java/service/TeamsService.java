package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.*;
import com.java.entity.*;
import com.java.utils.DateUtils;
import com.java.utils.IDUtils;
import com.java.utils.StringUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("teamsService")
public class TeamsService {

    @Resource
    private UsersDao usersDao;

    @Resource
    private TeamTypesDao teamTypesDao;

    @Resource
    private TeamsDao teamsDao;

    @Resource
    private MembersDao membersDao;

    @Resource
    private NoticesDao noticesDao;

    @Resource
    private ActivitiesDao activitiesDao;

    @Resource
    private ActiveLogsDao activeLogsDao;

    @Resource
    private ApplyLogsDao applyLogsDao;

    @Resource
    private PayLogsDao payLogsDao;

    @Resource
    private ApplyDao applyDao;


    @Transactional
    public void add(Teams teams) {
        teamsDao.insert(teams);
        Members member = new Members();
        member.setId(IDUtils.makeIDByCurrent());
        member.setUserId(teams.getManager());
        member.setTeamId(teams.getId());
        member.setCreateTime(DateUtils.getNowDate());
        membersDao.insert(member);
        Users user = usersDao.selectById(teams.getManager());
        user.setType(1);
        usersDao.updateById(user);
    }


    public void update(Teams teams) {
        teamsDao.updateById(teams);
    }


    @Transactional
    public void delete(Teams teams) {

        QueryWrapper<Notices> qw_notice = new QueryWrapper<>();
        qw_notice.eq("team_id", teams.getId());
        noticesDao.delete(qw_notice);

        QueryWrapper<PayLogs> qw_pay = new QueryWrapper<>();
        qw_pay.eq("team_id", teams.getId());
        payLogsDao.delete(qw_pay);

        QueryWrapper<ApplyLogs> qw_apply = new QueryWrapper<>();
        qw_apply.eq("team_id", teams.getId());
        applyLogsDao.delete(qw_apply);

        QueryWrapper<Members> qw_members = new QueryWrapper<>();
        qw_members.eq("team_id", teams.getId());
        membersDao.delete(qw_members);

        QueryWrapper<Activities> qw_active = new QueryWrapper<>();
        qw_active.eq("team_id", teams.getId());
        for (Activities activitie : activitiesDao.selectList(qw_active)) {

            QueryWrapper<ActiveLogs> qw_active_log = new QueryWrapper<>();
            qw_active_log.eq("active_id", activitie.getId());
            activeLogsDao.delete(qw_active_log);
        }
        activitiesDao.delete(qw_active);

        teamsDao.deleteById(teams);

        QueryWrapper<Teams> qw_team = new QueryWrapper<>();
        qw_team.eq("manager", teams.getManager());
        if (teamsDao.selectCount(qw_team) <= 0) {

            Users user = usersDao.selectById(teams.getManager());
            user.setType(2);
            usersDao.updateById(user);
        }
    }


    public Teams getOne(String id) {
        return teamsDao.selectById(id);
    }


    public List<Teams> getAll() {
        LambdaQueryWrapper<Teams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Teams::getCreateTime);
        return teamsDao.selectList(queryWrapper);
    }


    public List<Teams> getListByManId(String manId) {
        LambdaQueryWrapper<Teams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teams::getManager, manId).orderByDesc(Teams::getCreateTime);
        return teamsDao.selectList(queryWrapper);
    }


    public PageData getPageInfo(Long pageIndex, Long pageSize, String name, String typeId, String manager) {
        LambdaQueryWrapper<Teams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotNullOrEmpty(name), Teams::getName, name)
                .eq(StringUtils.isNotNullOrEmpty(typeId), Teams::getTypeId, typeId)
                .eq(StringUtils.isNotNullOrEmpty(manager), Teams::getManager, manager)
                .orderByDesc(Teams::getCreateTime);
        Page<Teams> page = teamsDao.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Teams> p) {

        List<Map<String, Object>> resl = new ArrayList<>();

        for (Teams teams : p.getRecords()) {

            Map<String, Object> temp = new HashMap<>();
            temp.put("id", teams.getId());
            temp.put("name", teams.getName());
            temp.put("createTime", teams.getCreateTime());
            temp.put("total", teams.getTotal());

            Users user = usersDao.selectById(teams.getManager());
            temp.put("manager", teams.getManager());
            temp.put("managerName", user.getName());
            temp.put("managerPhone", user.getPhone());
            temp.put("managerAddress", user.getAddress());

            TeamTypes teamType = teamTypesDao.selectById(teams.getTypeId());
            temp.put("typeId", teams.getTypeId());
            temp.put("typeName", teamType.getName());
            resl.add(temp);
        }

        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);
    }

    public PageData parsePageV2(Page<Apply> p) {
        List<Map<String, Object>> resl = new ArrayList<>();
        for (Apply apply : p.getRecords()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", apply.getId());
            temp.put("userId", apply.getUserId());
            temp.put("teamId", apply.getTeamId());
            temp.put("success", apply.getSuccess());
            resl.add(temp);
        }
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);
    }


    public PageData getApplyPage(String userId, Long pageSize, Long pageIndex) {
        // 获取团队信息列表，可能会有多个团队满足条件
        List<Teams> teamsList = teamsDao.selectList(new QueryWrapper<Teams>().eq("manager", userId));
        // 如果没有找到符合条件的团队信息，则直接返回空列表
        if (teamsList.isEmpty()) {
            return new PageData(pageIndex, pageSize, 0L, new ArrayList<>());
        }
        // 构造团队ID列表，用于查询申请记录
        List<String> teamIds = teamsList.stream().map(Teams::getId).collect(Collectors.toList());

        // 构造查询条件：申请成功为0（未处理），并且团队ID在指定的团队ID列表中
        LambdaQueryWrapper<Apply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Apply::getSuccess, "0")
                .in(Apply::getTeamId, teamIds);
        // 执行分页查询
        Page<Apply> page = applyDao.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return parsePageV2(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deal(Apply apply) {
        applyDao.updateById(apply);
        if ("1".equals(apply.getSuccess())){
            Members members = new Members();
            members.setId(IDUtils.makeIDByCurrent());
            members.setTeamId(apply.getTeamId());
            members.setUserId(apply.getUserId());
            membersDao.insert(members);
        }

    }
}