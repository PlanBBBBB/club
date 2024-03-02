package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文件下载DTO")
public class FileDto {
    @ApiModelProperty(value = "文件名")
    private String name;
}
