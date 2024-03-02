package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("通用idDto")
@Data
public class IdDto {
    @ApiModelProperty(value = "id")
    private String id;
}
