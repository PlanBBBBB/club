package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "apply")
@ApiModel("申请加入社团表")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "主键ID")
    private String id;

    @TableField(value = "team_id")
    @ApiModelProperty(value = "加入社团")
    private String teamId;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "申请用户")
    private String userId;

    @TableField(value = "success")
    @ApiModelProperty(value = "是否成功(0：未成功，1：成功，2：拒绝)")
    private String success;

}
