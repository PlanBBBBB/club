package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PwdDto {
    @ApiModelProperty(value = "密码")
    String password;
    @ApiModelProperty(value = "token")
    String token;
}
