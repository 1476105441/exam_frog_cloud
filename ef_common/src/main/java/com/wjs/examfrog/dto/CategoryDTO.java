package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryDTO {
    @ApiModelProperty(value = "分区id，为空查询全部；创建时必须带上")
    private Long areaId;
    @ApiModelProperty(value = "标签名称，为空查询全部，输入完整名称；创建时必须带上")
    private String categoryName;
    @ApiModelProperty(value = "状态，1是上线，2是下线，为空查询全部；创建时设置为1")
    private Long status;
}
