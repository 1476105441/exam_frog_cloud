package com.wjs.examfrog.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "轮播图对象")
public class SlideShowDTO implements Serializable {

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "分区名称")
    private String kind;

    @ApiModelProperty("跳转地址")
    private String skipUrl;

    @ApiModelProperty("当前状态，1-上线 0-下线")
    private Integer status;

}
