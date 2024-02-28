package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "activities")
@ApiModel("活动信息")
public class Activities implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id")
	@ApiModelProperty(value = "记录ID")
	private String id;

	@TableField(value = "name")
	@ApiModelProperty(value = "活动名称")
	private String name;

	@TableField(value = "comm")
	@ApiModelProperty(value = "活动概述")
	private String comm;

	@TableField(value = "detail")
	@ApiModelProperty(value = "活动详情")
	private String detail;

	@TableField(value = "ask")
	@ApiModelProperty(value = "活动要求")
	private String ask;

	@TableField(value = "total")
	@ApiModelProperty(value = "报名人数")
	private Integer total;

	@TableField(value = "active_time")
	@ApiModelProperty(value = "活动时间")
	private String activeTime;

	@TableField(value = "team_id")
	@ApiModelProperty(value = "发布社团")
	private String teamId;
}