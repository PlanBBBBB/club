package com.java.dto;

import com.java.entity.ApplyLogs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("添加申请日志DTO")
public class AddApplyLogsDto extends ApplyLogs {
    @ApiModelProperty(value = "token")
    private String token;
}
