package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "用户id", position = -99)
    private Long id;
    @ApiModelProperty(value = "用户昵称", position = -98)
    private String nickName;
    @ApiModelProperty(value = "头像", position = -97)
    private String avatarUrl;
    @ApiModelProperty(value = "性别", position = -96)
    private Integer gender;
    @ApiModelProperty(value = "语言", position = -95)
    private String language;
    @ApiModelProperty(value = "国家", position = -94)
    private String country;
    @ApiModelProperty(value = "省份", position = -93)
    private String province;
    @ApiModelProperty(value = "城市", position = -92)
    private String city;
    @ApiModelProperty(value = "关注数")
    private Long followCount;
    @ApiModelProperty(value = "粉丝数")
    private Long fansCount;
    @ApiModelProperty(value = "个性签名")
    private String signature;
    @ApiModelProperty(value = "电话号码")
    private Long phone;
    @ApiModelProperty(value = "最近登录时间")
    private LocalDateTime loginTime;
    @ApiModelProperty(value = "状态",notes = "为真则为冻结状态")
    private Boolean freeze;
    @ApiModelProperty(value = "注册时间")
    private LocalDateTime gmtCreate;
}
