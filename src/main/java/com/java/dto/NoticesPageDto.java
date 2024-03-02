package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("通知分页DTO")
public class NoticesPageDto extends PageDto{
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("团队名称")
    String teamName;
    @ApiModelProperty("标题")
    String title;
}
