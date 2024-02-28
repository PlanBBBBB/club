package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "active_logs")
@ApiModel("报名记录")
public class ActiveLogs implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id")
	@ApiModelProperty(value = "记录ID")
	private String id;

	@TableField(value = "create_time")
	@ApiModelProperty(value = "报名时间")
	private String createTime;

	@TableField(value = "active_id")
	@ApiModelProperty(value = "活动编号")
	private String activeId;

	@TableField(value = "user_id")
	@ApiModelProperty(value = "报名用户")
	private String userId;
}