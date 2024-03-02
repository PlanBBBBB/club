package com.java.dto;

import com.java.entity.Teams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社团分页查询DTO")
public class TeamsPageDto extends PageDto {
    @ApiModelProperty("token")
    String token;
    @ApiModelProperty("社团")
    Teams teams;
}
