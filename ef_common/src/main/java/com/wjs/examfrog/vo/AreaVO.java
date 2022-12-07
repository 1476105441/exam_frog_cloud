package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaVO {
    @ApiModelProperty(value = "分区id")
    private Long id;

    @ApiModelProperty(value = "分区名称")
    private String name;

    @ApiModelProperty(value = "是否下线")
    private Boolean isDelete;

    @ApiModelProperty(value = "内容数量")
    private Long count;
}
