package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "teams")
@ApiModel("社团信息")
public class Teams implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "记录ID")
    private String id;

    @TableField(value = "name")
    @ApiModelProperty(value = "社团名称")
    private String name;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "建立时间")
    private String createTime;

    @TableField(value = "total")
    @ApiModelProperty(value = "社团人数")
    private Integer total;

    @TableField(value = "manager")
    @ApiModelProperty(value = "社团团长")
    private String manager;

    @TableField(value = "type_id")
    @ApiModelProperty(value = "社团编号")
    private String typeId;
}