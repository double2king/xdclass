package com.def.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户登录对象")
@Data
public class UserLoginRequest {

    @ApiModelProperty(value = "邮箱", example = "123@qq.com")
    private String mail;
    @ApiModelProperty(value = "密码", example = "123")
    private String pwd;

}
