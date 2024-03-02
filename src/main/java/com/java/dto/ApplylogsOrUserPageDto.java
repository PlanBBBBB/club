package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查找申请记录、成员或缴费记录Dto")
public class ApplylogsOrUserPageDto extends PageDto{
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("团队名称")
    String teamName;
    @ApiModelProperty("用户名称")
    String userName;
}
