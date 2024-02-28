package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "apply_logs")
@ApiModel("申请记录")
public class ApplyLogs implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id")
	@ApiModelProperty("记录ID")
	private String id;

	@TableField(value = "status")
	@ApiModelProperty("处理状态")
	private Integer status;

	@TableField(value = "create_time")
	@ApiModelProperty("申请时间")
	private String createTime;

	@TableField(value = "team_id")
	@ApiModelProperty("申请社团")
	private String teamId;

	@TableField(value = "user_id")
	@ApiModelProperty("申请用户")
	private String userId;
}