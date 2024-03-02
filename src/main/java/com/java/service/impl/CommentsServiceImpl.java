package com.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java.dao.CommentsDao;
import com.java.entity.Comments;
import com.java.service.CommentsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("commentsService")
public class CommentsServiceImpl implements CommentsService {

    @Resource
    private CommentsDao commentsDao;

    @Override
    public List<Comments> getCommByUserId(String id) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getUsersId, id);
        return commentsDao.selectList(queryWrapper);
    }

    @Override
    public List<Comments> getCommByActId(String id) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getActivitiesId, id);
        return commentsDao.selectList(queryWrapper);
    }

    @Override
    public void save(Comments comments) {
        commentsDao.insert(comments);
    }

    @Override
    public void delete(String id) {
        commentsDao.deleteById(id);
    }
}
