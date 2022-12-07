package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetailsVO implements Serializable {

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

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "关注数")
    private Long followCount;
    @ApiModelProperty(value = "粉丝数")
    private Long fansCount;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "获赞数")
    private Long likeCount;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "收藏数")
    private Long favCount;

    @ApiModelProperty(value = "帖子数")
    private Long postCount;

}
