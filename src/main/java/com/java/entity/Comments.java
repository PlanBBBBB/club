package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "comments")
@ApiModel("评论表")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "评论ID")
    private String id;

    @TableField(value = "activities_id")
    @ApiModelProperty(value = "活动ID")
    private String activitiesId;

    @TableField(value = "users_id")
    @ApiModelProperty(value = "评论的用户ID")
    private String usersId;

    @TableField(value = "content")
    @ApiModelProperty(value = "评论内容")
    private String content;

    @TableField(value = "created")
    @ApiModelProperty(value = "创建时间")
    private String created;
}
