package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java.dao.ActiveLogsDao;
import com.java.dao.ActivitiesDao;
import com.java.dao.UsersDao;
import com.java.entity.ActiveLogs;
import com.java.entity.Activities;
import com.java.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activeLogsService")
public class ActiveLogsService {

    @Resource
    private UsersDao usersDao;

    @Resource
    private ActiveLogsDao activeLogsDao;

    @Resource
    private ActivitiesDao activitiesDao;
    
    @Transactional
    public void add(ActiveLogs activeLogs) {
        Activities activitie = activitiesDao.selectById(activeLogs.getActiveId());
        activitie.setTotal(activitie.getTotal() + 1);
        activitiesDao.updateById(activitie);
        activeLogsDao.insert(activeLogs);
    }

    
    public void update(ActiveLogs activeLogs) {
        activeLogsDao.updateById(activeLogs);
    }

    
    public void delete(ActiveLogs activeLogs) {
        activeLogsDao.deleteById(activeLogs);
    }

    
    public Boolean isActive(String activeId, String userId){
        LambdaQueryWrapper<ActiveLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActiveLogs::getActiveId, activeId).eq(ActiveLogs::getUserId, userId);
        return activeLogsDao.selectCount(queryWrapper) <= 0;
    }

    
    public ActiveLogs getOne(String id) {
        return activeLogsDao.selectById(id);
    }

    
    public List<Map<String, Object>> getListByActiveId(String activeId){

        List<Map<String, Object>> resl = new ArrayList<>();

        LambdaQueryWrapper<ActiveLogs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ActiveLogs::getActiveId, activeId).orderByDesc(ActiveLogs::getCreateTime);
        List<ActiveLogs> activeLogs = activeLogsDao.selectList(queryWrapper);

        for (ActiveLogs activeLog : activeLogs) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", activeLog.getId());
            temp.put("createTime", activeLog.getCreateTime());
            temp.put("activeId", activeLog.getActiveId());

            Users user = usersDao.selectById(activeLog.getUserId());
            temp.put("userId", activeLog.getUserId());
            temp.put("userName", user.getName());
            temp.put("userGender", user.getGender());
            temp.put("userPhone", user.getPhone());
            resl.add(temp);
        }
        return resl;
    }
}