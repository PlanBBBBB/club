package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "notices")
@ApiModel("通知记录")
public class Notices implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "记录ID")
    private String id;

    @TableField(value = "title")
    @ApiModelProperty(value = "通知标题")
    private String title;

    @TableField(value = "detail")
    @ApiModelProperty(value = "通知详情")
    private String detail;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "发布时间")
    private String createTime;

    @TableField(value = "team_id")
    @ApiModelProperty(value = "发布社团")
    private String teamId;
}