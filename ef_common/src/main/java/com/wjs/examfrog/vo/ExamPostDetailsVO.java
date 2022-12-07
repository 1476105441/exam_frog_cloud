package com.wjs.examfrog.vo;

import com.wjs.examfrog.dto.UrlDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamPostDetailsVO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "考试帖子id", position = -99)
    private Long id;
    @ApiModelProperty(value = "考试帖子标题", position = -98)
    private String title;
    @ApiModelProperty(value = "访问量")
    private Long viewCount;
    @ApiModelProperty(value = "正在尝试的人数")
    private Long tryingCount;
    @ApiModelProperty(value = "点赞数")
    private Long likeCount;
    @ApiModelProperty(value = "评论数")
    private Long commentCount;
    @ApiModelProperty(value = "收藏数")
    private Long favCount;
    @ApiModelProperty(value = "分享数")
    private Long shareCount;
    @ApiModelProperty(value = "标签")
    private String tags;
    @ApiModelProperty(value = "摘要")
    private String summary;
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
    @ApiModelProperty(value = "是否完成")
    private Boolean completed;

    /**
     * 联表查询
     */
    @ApiModelProperty(value = "分类名称", position = -97)
    private String categoryName;
    @ApiModelProperty(value = "考试开始时间", position = -96)
    private LocalDateTime startTime;
    @ApiModelProperty(value = "考试结束时间", position = -95)
    private LocalDateTime endTime;

    /**
     * 逻辑判断
     */
    @ApiModelProperty(value = "状态 0是下线，1是上线，2是草稿")
    private Long status;

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "考试帖子内容")
    private String content;
    @ApiModelProperty(value = "头图url")
    private String titleImageUrl;
    @ApiModelProperty(value = "链接数组")
    private List<UrlDTO> linkUrlList;

}
