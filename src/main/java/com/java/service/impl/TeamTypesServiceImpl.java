package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.TeamTypesDao;
import com.java.dao.TeamsDao;
import com.java.entity.TeamTypes;
import com.java.entity.Teams;
import com.java.service.TeamTypesService;
import com.java.utils.StringUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("teamTypesService")
public class TeamTypesServiceImpl implements TeamTypesService {

    @Resource
    private TeamTypesDao teamTypesDao;

    @Resource
    private TeamsDao teamsDao;

    @Override
    @Transactional
    public void add(TeamTypes teamTypes) {

        teamTypesDao.insert(teamTypes);
    }

    @Override
    @Transactional
    public void update(TeamTypes teamTypes) {

        teamTypesDao.updateById(teamTypes);
    }

    @Override
    @Transactional
    public void delete(TeamTypes teamTypes) {

        teamTypesDao.deleteById(teamTypes);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean isRemove(String typeId){

        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("type_id", typeId);

        return teamsDao.selectCount(qw) <= 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public TeamTypes getOne(String id) {

        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();
        qw.orderByDesc("create_time");

        TeamTypes teamTypes = teamTypesDao.selectById(id);

        return teamTypes;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<TeamTypes> getAll() {

        List<TeamTypes> list = teamTypesDao.selectList(null);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, TeamTypes teamTypes) {

        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();

        if (StringUtils.isNotNullOrEmpty(teamTypes.getName())) {

            qw.like("name", teamTypes.getName());
        }

        qw.orderByDesc("create_time");

        Page<TeamTypes> page =
                teamTypesDao.selectPage(new Page<TeamTypes>(pageIndex, pageSize), qw);

        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<TeamTypes> p) {

        List<Map<String, Object>> resl = new ArrayList<Map<String, Object>>();

        for (TeamTypes teamTypes : p.getRecords()) {

            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", teamTypes.getId());
            temp.put("name", teamTypes.getName());
            temp.put("createTime", teamTypes.getCreateTime());
            resl.add(temp);
        }

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);

        return pageData;
    }
}