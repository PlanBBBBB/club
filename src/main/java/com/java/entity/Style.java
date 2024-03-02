package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "style")
@ApiModel("活动信息")
public class Style implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "风采ID")
    private String id;

    @TableField(value = "name")
    @ApiModelProperty(value = "风采名称")
    private String name;

    @TableField(value = "comm")
    @ApiModelProperty(value = "风采概述")
    private String comm;

    @TableField(value = "detail")
    @ApiModelProperty(value = "风采详情")
    private String detail;

    @TableField(value = "ask")
    @ApiModelProperty(value = "风采要求")
    private String ask;

    @TableField(value = "total")
    @ApiModelProperty(value = "报名人数")
    private Integer total;

    @TableField(value = "active_time")
    @ApiModelProperty(value = "风采时间")
    private String activeTime;

    @TableField(value = "team_id")
    @ApiModelProperty(value = "发布社团")
    private String teamId;
}