package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社长查询申请加入社团记录DTO")
public class TokenPageDto extends PageDto {
    @ApiModelProperty("token")
    String token;
}
