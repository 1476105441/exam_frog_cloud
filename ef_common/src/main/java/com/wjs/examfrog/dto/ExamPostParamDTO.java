package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamPostParamDTO {

    @ApiModelProperty(value = "考试帖子标题", required = true, position = -98)
    @NotNull(message = "不能为空")
    private String title;

    @ApiModelProperty(value = "帖子id",notes = "更改帖子填上帖子id，将之前未完成草稿进行发布时，填上草稿id。正常发布和保存草稿此id为空即可")
    private Long id;

    @ApiModelProperty(value = "标签", required = true)
    @NotNull(message = "不能为空")
    private String tags;

    @ApiModelProperty(value = "摘要", required = true)
    @NotNull(message = "不能为空")
    private String summary;

    @ApiModelProperty(value = "考试帖子内容", required = true)
    @NotNull(message = "不能为空")
    private String content;

    @ApiModelProperty(value = "头图url", required = true)
    @NotNull(message = "不能为空")
    private String titleImageUrl;

    @ApiModelProperty(value = "链接数组", required = true)
    @NotNull(message = "不能为空")
    private List<UrlDTO> linkUrlList;

    @ApiModelProperty(value = "类别id", required = true)
    @NotNull(message = "不能为空")
    private Long categoryId;

    @ApiModelProperty(value = "考试开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "考试结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "是否完成", required = true,notes = "true就是发布文章，false就是保存草稿")
    @NotNull(message = "不能为空")
    private Boolean completed;
}
