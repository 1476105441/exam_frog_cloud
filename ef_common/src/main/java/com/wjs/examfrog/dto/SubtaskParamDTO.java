package com.wjs.examfrog.dto;

import com.wjs.examfrog.bo.ValidList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接收 Subtask DTO
 */
@Data
@Valid
public class SubtaskParamDTO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "目标标题", required = true, position = -98)
    @NotNull(message = "不能为空")
    private String title;
    @ApiModelProperty(value = "subtask 的下标 (保证不重复, 数字连续, 从0开始)", required = true, position = -97)
    @NotNull(message = "不能为空")
    private Integer idx;
    @ApiModelProperty(value = "目标层级", required = true, position = -96)
    @NotNull(message = "不能为空")
    private Integer layer;
    /**
     * 允许为空
     */
    @ApiModelProperty(value = "目标开始时间", required = false, position = -95)
    private LocalDateTime startTime;
    /**
     * 允许为空
     */
    @ApiModelProperty(value = "目标结束时间", required = false, position = -94)
    private LocalDateTime endTime;

    /**
     * swagger: 默认位置
     */
    @ApiModelProperty(value = "目标内容", required = true)
    @NotNull(message = "不能为空")
    private String content;
    @ApiModelProperty(value = "目标的父节点idx (pidx = -1: 表示没有父节点)", required = true)
    @NotNull(message = "不能为空")
    private Integer pidx;

    /**
     * swagger: 置底
     */
    /**
     * 联表
     */
    @ApiModelProperty(value = "文件数组", required = true, position = 100)
    @NotNull(message = "不能为空")
    @Valid
    private ValidList<UrlParamDTO> fileUrlList;
    /**
     * 联表
     */
    @ApiModelProperty(value = "图片数组", required = true, position = 101)
    @NotNull(message = "不能为空")
    @Valid
    private ValidList<UrlParamDTO> imageUrlList;
    /**
     * 联表
     */
    @ApiModelProperty(value = "链接数组", required = true, position = 102)
    @NotNull(message = "不能为空")
    @Valid
    private ValidList<UrlParamDTO> linkUrlList;

}
