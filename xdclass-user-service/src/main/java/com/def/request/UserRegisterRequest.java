package com.def.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserRegisterRequest {

    @ApiModelProperty(value = "姓名", example = "张三")
    private String name;
    @ApiModelProperty(value = "密码", example = "pwd")
    private String pwd;
    @ApiModelProperty(value = "性别(0代表男，1代表女)", example = "0")
    private String sex;
    @ApiModelProperty(value = "邮箱", example = "123@")
    private String mail;
    @ApiModelProperty(value = "验证码", example = "1111")
    private String code;

}
