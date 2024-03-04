package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社团类型分页查询DTO")
public class TeamTypesPageDto  extends PageDto{
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("类型名称")
    private String name;
}
