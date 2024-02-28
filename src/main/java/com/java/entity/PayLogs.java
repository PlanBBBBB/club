package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "pay_logs")
@ApiModel("缴费记录")
public class PayLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty("记录ID")
    private String id;

    @TableField(value = "create_time")
    @ApiModelProperty("缴费时间")
    private String createTime;

    @TableField(value = "total")
    @ApiModelProperty("缴纳费用")
    private Double total;

    @TableField(value = "team_id")
    @ApiModelProperty("收费社团")
    private String teamId;

    @TableField(value = "user_id")
    @ApiModelProperty("缴费用户")
    private String userId;
}