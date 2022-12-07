package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 接收 Url DTO
 */
@Data
public class UrlParamDTO implements Serializable {

    @ApiModelProperty(value = "超链接名字", required = true, position = 2)
    @NotNull(message = "不能为空")
    private String name;
    @ApiModelProperty(value = "超链接", required = true, position = 2)
    @NotNull(message = "不能为空")
    private String url;

}
