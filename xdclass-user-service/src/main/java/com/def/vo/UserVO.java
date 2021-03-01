package com.def.vo;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户展示数据")
public class UserVO {
    /**
     * 昵称
     */
    @ApiModelProperty(value = "名字", example = "zs")
    private String name;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String headImg;

    /**
     * 用户签名
     */
    @ApiModelProperty("签名")
    private String slogan;

    /**
     * 0表示女，1表示男
     */
    @ApiModelProperty("性别")
    private Integer sex;
    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer points;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String mail;
}
