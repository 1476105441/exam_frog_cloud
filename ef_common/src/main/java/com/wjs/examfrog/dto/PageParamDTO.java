package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 接收 分页信息 DTO
 */
@Data
public class PageParamDTO implements Serializable {

    @ApiModelProperty(value = "当前页码", required = true, position = 1)
    @NotNull(message = "不能为空")
    private Integer pageNum;
    @ApiModelProperty(value = "每页数量", required = true, position = 2)
    @NotNull(message = "不能为空")
    private Integer pageSize;

}
