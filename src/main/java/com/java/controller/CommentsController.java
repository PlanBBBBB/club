package com.java.controller;

import com.java.dto.IdDto;
import com.java.entity.Comments;
import com.java.service.CommentsService;
import com.java.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 申请记录
 */
@Controller
@RequestMapping("/comments")
@Api(tags = "评论")
public class CommentsController {

    @Resource
    private CommentsService commentsService;

    @PostMapping("/getByUserId")
    @ResponseBody
    @ApiOperation("根据用户id查询所有评论")
    public R getByUserId(@RequestBody IdDto idDto) {
        List<Comments> commentsList = commentsService.getCommByUserId(idDto.getId());
        return R.successData(commentsList);
    }

    @PostMapping("/getByActId")
    @ResponseBody
    @ApiOperation("根据活动id查询所有评论")
    public R getByActId(@RequestBody IdDto idDto) {
        List<Comments> commentsList = commentsService.getCommByActId(idDto.getId());
        return R.successData(commentsList);
    }

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("添加评论")
    public R save(@RequestBody Comments comments) {
        commentsService.save(comments);
        return R.success();
    }


    @PostMapping("/del")
    @ResponseBody
    @ApiOperation("删除评论")
    public R del(@RequestBody IdDto idDto) {
        commentsService.delete(idDto.getId());
        return R.success();
    }
}
