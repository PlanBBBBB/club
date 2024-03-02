package com.java.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页DTO")
public class PageDto {
    @ApiModelProperty("页码")
    private Long pageIndex;

    @ApiModelProperty("每页数量")
    private Long pageSize;
}
