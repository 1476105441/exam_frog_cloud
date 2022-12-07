package com.wjs.examfrog.vo;

import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.UrlDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserPostManageDetailsVO {
    @ApiModelProperty(value = "用户帖子id", position = -99)
    private Long id;
    @ApiModelProperty(value = "用户帖子标题", position = -98)
    private String title;
    @ApiModelProperty(value = "作者id", position = -97)
    private Long authorId;
    @ApiModelProperty(value = "作者昵称")
    private String authorNickName;
    @ApiModelProperty(value = "作者头像")
    private String authorAvatarUrl;
    @ApiModelProperty(value = "分类id",  position = -96)
    private String categoryId;
    @ApiModelProperty(value = "访问量")
    private Long viewCount;
    @ApiModelProperty(value = "评论数量")
    private Long commentCount;
    @ApiModelProperty(value = "分享量")
    private Long shareCount;
    @ApiModelProperty(value = "分区名称")
    private String areaName;

    /**
     * 联表查询
     */
    @ApiModelProperty(value = "分类名称",  position = -94)
    private String categoryName;
    @ApiModelProperty(value = "标签",  position = -93)
    private String tags;
    @ApiModelProperty(value = "最后修改时间",  position = -92)
    private LocalDateTime gmtModified;
    @ApiModelProperty(value = "发帖时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty(value = "收藏数")
    private Long favCount;
    @ApiModelProperty(value = "链接数组", position = 100)
    private List<UrlDTO> linkUrlList;
    @ApiModelProperty(value = "目标数组 (已按 idx 排序)", position = 101)
    private List<SubtaskDTO> subtaskList;

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "用户帖子内容")
    private String content;
    @ApiModelProperty(value = "头图url")
    private String titleImageUrl;
    @ApiModelProperty(value = "复用数")
    private Long usedCount;
    @ApiModelProperty(value = "状态 0是下线，1是上线，2是草稿")
    private Long status;
}
