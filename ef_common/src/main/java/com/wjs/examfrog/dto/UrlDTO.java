package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回 Url DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDTO implements Serializable {

    @ApiModelProperty(value = "超链接名字", position = 2)
    private String name;
    @ApiModelProperty(value = "超链接", position = 3)
    private String url;

}
