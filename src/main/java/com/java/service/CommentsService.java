package com.java.service;

import com.java.entity.Comments;

import java.util.List;

public interface CommentsService {
    List<Comments> getCommByUserId(String id);

    List<Comments> getCommByActId(String id);

    void save(Comments comments);

    void delete(String id);

}
