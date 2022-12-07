package com.wjs.examfrog.dto;

import com.wjs.examfrog.bo.ValidList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 接收 UserPost DTO
 */
@Data
public class UserPostParamDTO implements Serializable {

    /**
     * swagger置顶
     */
    @ApiModelProperty(value = "用户帖子标题", required = true, position = -98)
    @NotNull(message = "不能为空")
    private String title;
    @ApiModelProperty(value = "分类id", required = true,  position = -95)
    @NotNull(message = "不能为空")
    private Long categoryId;
    @ApiModelProperty(value = "标签", required = true,  position = -94)
    @NotNull(message = "不能为空")
    private String tags;

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "用户帖子内容", required = true)
    @NotNull(message = "不能为空")
    private String content;
    @ApiModelProperty(value = "头图url", required = true)
    @NotNull(message = "不能为空")
    private String titleImageUrl;
    @ApiModelProperty(value = "是否为原创帖子", required = true)
    @NotNull(message = "不能为空")
    private Boolean isOriginal;

    /**
     * swagger: 置底
     */
    /**
     * 联表
     */
    @ApiModelProperty(value = "用户帖子内容", required = true, position = 100)
    @NotNull(message = "不能为空")
    private ValidList<SubtaskParamDTO> subtaskList;

}
