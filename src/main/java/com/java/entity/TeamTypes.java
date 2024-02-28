package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "team_types")
@ApiModel("社团类型")
public class TeamTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty("记录ID")
    private String id;

    @TableField(value = "name")
    @ApiModelProperty("类型名称")
    private String name;

    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private String createTime;
}