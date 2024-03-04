package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.TeamTypesDao;
import com.java.dao.TeamsDao;
import com.java.entity.TeamTypes;
import com.java.entity.Teams;
import com.java.service.TeamTypesService;
import com.java.utils.StringUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;

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
    public void add(TeamTypes teamTypes) {
        teamTypesDao.insert(teamTypes);
    }

    @Override
    public void update(TeamTypes teamTypes) {
        teamTypesDao.updateById(teamTypes);
    }

    @Override
    public void delete(TeamTypes teamTypes) {
        teamTypesDao.deleteById(teamTypes);
    }

    @Override
    public Boolean isRemove(String typeId) {
        LambdaQueryWrapper<Teams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teams::getTypeId, typeId);
        return teamsDao.selectCount(queryWrapper) <= 0;
    }

    @Override
    public TeamTypes getOne(String id) {
        return teamTypesDao.selectById(id);
    }

    @Override
    public List<TeamTypes> getAll() {
        return teamTypesDao.selectList(null);
    }

    @Override
    public PageData getPageInfo(Long pageIndex, Long pageSize, String name) {
        LambdaQueryWrapper<TeamTypes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotNullOrEmpty(name), TeamTypes::getName, name)
                .orderByDesc(TeamTypes::getCreateTime);
        Page<TeamTypes> page = teamTypesDao.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<TeamTypes> p) {
        List<Map<String, Object>> resl = new ArrayList<>();
        for (TeamTypes teamTypes : p.getRecords()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", teamTypes.getId());
            temp.put("name", teamTypes.getName());
            temp.put("createTime", teamTypes.getCreateTime());
            resl.add(temp);
        }
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);
    }
}