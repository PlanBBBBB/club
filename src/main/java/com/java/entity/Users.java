package com.java.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "users")
@ApiModel("系统用户")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @ApiModelProperty(value = "记录ID")
    private String id;

    @TableField(value = "user_name")
    @ApiModelProperty(value = "用户账号")
    private String userName;

    @TableField(value = "pass_word")
    @ApiModelProperty(value = "用户密码")
    private String passWord;

    @TableField(value = "name")
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @TableField(value = "gender")
    @ApiModelProperty(value = "用户性别")
    private String gender;

    @TableField(value = "age")
    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @TableField(value = "phone")
    @ApiModelProperty(value = "联系电话")
    private String phone;

    @TableField(value = "address")
    @ApiModelProperty(value = "联系地址")
    private String address;

    @TableField(value = "status")
    @ApiModelProperty(value = "信息状态")
    private Integer status;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "添加时间")
    private String createTime;

    @TableField(value = "type")
    @ApiModelProperty(value = "用户身份")
    private Integer type;
}