package com.java.dto;

import com.java.entity.TeamTypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社团类型分页查询DTO")
public class TeamTypesPageDto  extends PageDto{
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("社团类型")
    TeamTypes teamTypes;
}
