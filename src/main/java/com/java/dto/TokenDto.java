package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("统一tokenDto")
public class TokenDto {
    @ApiModelProperty("token")
    private String token;
}
