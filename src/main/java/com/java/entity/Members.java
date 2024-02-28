package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "members")
@ApiModel("成员信息")
public class Members implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id")
	@ApiModelProperty(value = "记录ID")
	private String id;

	@TableField(value = "create_time")
	@ApiModelProperty(value = "入团时间")
	private String createTime;

	@TableField(value = "team_id")
	@ApiModelProperty(value = "加入社团")
	private String teamId;

	@TableField(value = "user_id")
	@ApiModelProperty(value = "申请用户")
	private String userId;
}