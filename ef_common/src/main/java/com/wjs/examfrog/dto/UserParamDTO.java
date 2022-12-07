package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserParamDTO {
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    @ApiModelProperty(value = "电话号码")
    private Long phone;
    @ApiModelProperty(value = "最近登录时间:0(最近一周)，1(最近一月)，2(最近一年)")
    private Long loginTime;
    @ApiModelProperty(value = "状态,为true则为查询冻结状态，反之查询正常，为null查询全部")
    private Boolean freeze;
}
