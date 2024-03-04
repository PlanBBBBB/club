package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社团分页查询DTO")
public class TeamsPageDto extends PageDto {
    @ApiModelProperty("token")
    String token;

    @ApiModelProperty(value = "社团名称")
    private String name;

    @ApiModelProperty(value = "社团编号")
    private String typeId;

    @ApiModelProperty(value = "社团团长")
    private String manager;
}
