package com.java.dto;

import com.java.entity.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户列表分页查询DTO")
public class UsersPageDto extends PageDto {
    @ApiModelProperty("用户")
    private Users users;
}
