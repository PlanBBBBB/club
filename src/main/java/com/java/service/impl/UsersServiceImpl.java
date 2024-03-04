package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.dao.MembersDao;
import com.java.dao.UsersDao;
import com.java.entity.Members;
import com.java.entity.Users;
import com.java.service.UsersService;
import com.java.utils.StringUtils;
import com.java.vo.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    @Resource
    private MembersDao membersDao;

    @Resource
    private UsersDao usersDao;

    @Override
    public void add(Users users) {
        usersDao.insert(users);
    }

    @Override
    public void update(Users users) {
        usersDao.updateById(users);
    }

    @Override
    public void delete(Users users) {
        usersDao.deleteById(users);
    }

    @Override
    public Boolean isRemove(String userId) {
        LambdaQueryWrapper<Members> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Members::getUserId, userId);
        Integer total = membersDao.selectCount(queryWrapper);
        return total <= 0;
    }

    @Override
    public Users getOne(String id) {
        return usersDao.selectById(id);
    }

    @Override
    public Users getUserByUserName(String userName) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserName, userName);
        return usersDao.selectOne(queryWrapper);
    }

    @Override
    public PageData getPageInfo(Long pageIndex, Long pageSize, String name, String userName, String phone) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotNullOrEmpty(userName), Users::getUserName, userName)
                .like(StringUtils.isNotNullOrEmpty(name), Users::getName, name)
                .like(StringUtils.isNotNullOrEmpty(phone), Users::getPhone, phone)
                .orderByDesc(Users::getCreateTime);
        Page<Users> page = usersDao.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return parsePage(page);
    }


    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Users> p) {
        List<Map<String, Object>> resl = new ArrayList<>();
        for (Users users : p.getRecords()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", users.getId());
            temp.put("userName", users.getUserName());
            temp.put("passWord", users.getPassWord());
            temp.put("name", users.getName());
            temp.put("gender", users.getGender());
            temp.put("age", users.getAge());
            temp.put("phone", users.getPhone());
            temp.put("address", users.getAddress());
            temp.put("status", users.getStatus());
            temp.put("createTime", users.getCreateTime());
            temp.put("type", users.getType());
            resl.add(temp);
        }
        return new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);
    }
}