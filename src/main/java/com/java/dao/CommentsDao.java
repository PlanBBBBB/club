package com.java.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.entity.Comments;
import org.springframework.stereotype.Repository;

@Repository("CommentsDao")
public interface CommentsDao extends BaseMapper<Comments> {
}
