package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 接收 登录信息 DTO
 */
@Data
public class LoginParamDTO {

    @ApiModelProperty(value = "openId", required = true, position = -99)
    @NotNull(message = "不能为空")
    private String openId;
    @ApiModelProperty(value = "用户昵称", required = true, position = -98)
    @NotNull(message = "不能为空")
    private String nickName;
    @ApiModelProperty(value = "头像", required = true, position = -97)
    @NotNull(message = "不能为空")
    private String avatarUrl;
    @ApiModelProperty(value = "性别", required = true, position = -96)
    @NotNull(message = "不能为空")
    private Integer gender;
    @ApiModelProperty(value = "语言", required = true, position = -95)
    @NotNull(message = "不能为空")
    private String language;
    @ApiModelProperty(value = "国家", required = true, position = -94)
    @NotNull(message = "不能为空")
    private String country;
    @ApiModelProperty(value = "省份", required = true, position = -93)
    @NotNull(message = "不能为空")
    private String province;
    @ApiModelProperty(value = "城市", required = true, position = -92)
    @NotNull(message = "不能为空")
    private String city;

}
