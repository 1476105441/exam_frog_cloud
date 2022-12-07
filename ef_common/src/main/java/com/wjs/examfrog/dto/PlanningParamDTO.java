package com.wjs.examfrog.dto;

import com.wjs.examfrog.bo.ValidList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 接收 Planning DTO
 */
@Data
public class PlanningParamDTO implements Serializable {

    /**
     * swagger置顶
     */
    @ApiModelProperty(value = "title", required = true, position = -99)
    @NotNull(message = "不能为空")
    private String title;
    @ApiModelProperty(value = "content", required = true, position = -98)
    @NotNull(message = "不能为空")
    private String content;
    @ApiModelProperty(value = "tags", required = true, position = -97)
    @NotNull(message = "不能为空")
    private String tags;
    @ApiModelProperty(value = "followUserPostId ps:-1表示没有", required = true, position = -96)
    @NotNull(message = "不能为空")
    private Long followUserPostId;
    @ApiModelProperty(value = "titleImageUrl", required = true, position = -95)
    @NotNull(message = "不能为空")
    private String titleImageUrl;
    @ApiModelProperty(value = "categoryId", required = true, position = -94)
    @NotNull(message = "不能为空")
    private Long categoryId;

    /**
     * swagger: 置底
     */
    // 联表
    @ApiModelProperty(value = "用户帖子内容", required = true, position = 100)
    @NotNull(message = "不能为空")
    private ValidList<SubtaskParamDTO> subtaskList;

}
