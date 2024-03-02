package com.java.dto;

import com.java.entity.ActiveLogs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("添加活跃日志Dto")
public class AddActivelogsDto extends ActiveLogs {

    @ApiModelProperty("token")
    private String token;
}
