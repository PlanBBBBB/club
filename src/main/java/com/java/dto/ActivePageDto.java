package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查找活动信息Dto")
public class ActivePageDto extends PageDto {
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("团队名称")
    String teamName;
    @ApiModelProperty("活动名称")
    String activeName;
}
