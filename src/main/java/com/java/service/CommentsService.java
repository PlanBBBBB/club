package com.java.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java.dao.CommentsDao;
import com.java.entity.Comments;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("commentsService")
public class CommentsService {

    @Resource
    private CommentsDao commentsDao;

    
    public List<Comments> getCommByUserId(String id) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getUsersId, id);
        return commentsDao.selectList(queryWrapper);
    }

    
    public List<Comments> getCommByActId(String id) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getActivitiesId, id);
        return commentsDao.selectList(queryWrapper);
    }

    
    public void save(Comments comments) {
        commentsDao.insert(comments);
    }

    
    public void delete(String id) {
        commentsDao.deleteById(id);
    }
}
