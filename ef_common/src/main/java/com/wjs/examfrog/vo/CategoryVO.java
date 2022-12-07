package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryVO implements Serializable {

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "分类id", position = 1)
    private Long id;
    @ApiModelProperty(value = "分类名称", position = 2)
    private String name;
    @ApiModelProperty(value = "标签的位置",position = 3)
    private Long position;
    @ApiModelProperty(value = "是否下线",position = 3)
    private Boolean isDelete;
    @ApiModelProperty(value = "分区名称")
    private String areaName;
    @ApiModelProperty(value = "分区id")
    private Long areaId;
    @ApiModelProperty(value = "内容数量")
    private Long count;
}
