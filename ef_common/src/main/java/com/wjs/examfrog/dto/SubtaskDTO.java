package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 返回 Subtask DTO
 */
@Data
public class SubtaskDTO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "目标id", position = -99)
    private Long id;
    @ApiModelProperty(value = "目标标题", position = -98)
    private String title;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "subtask 的下标", position = -97)
    private Integer idx;
    @ApiModelProperty(value = "目标层级", position = -96)
    private Integer layer;
    @ApiModelProperty(value = "目标开始时间", position = -95)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "目标结束时间", position = -94)
    private LocalDateTime endTime;

    /**
     * swagger: 默认位置
     */
    @ApiModelProperty(value = "目标内容")
    private String content;
    @ApiModelProperty(value = "目标是否为非叶子节点")
    private Boolean isParent;
    @ApiModelProperty(value = "目标是否已完成")
    private Boolean isFinish;
    @ApiModelProperty(value = "目标的父节点id (pid = -1: 表示没有父节点)")
    private Long pid;

    /**
     * swagger: 置底
     */
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "文件数组", position = 100)
    private List<UrlDTO> fileUrlList;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "图片数组", position = 101)
    private List<UrlDTO> imageUrlList;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "链接数组", position = 102)
    private List<UrlDTO> linkUrlList;


}
