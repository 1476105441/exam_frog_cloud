package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserPostVO implements Serializable {

    /**
     * swagger: 置顶
     */
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

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "用户帖子内容")
    private String content;
    @ApiModelProperty(value = "头图url")
    private String titleImageUrl;
    @ApiModelProperty(value = "是否为原创帖子")
    private Boolean isOriginal;
    @ApiModelProperty(value = "是否经过认证")
    private Boolean isAuthentic;
    @ApiModelProperty(value = "正在尝试人数")
    private Long tryingCount;
    @ApiModelProperty(value = "已完成人数")
    private Long successCount;
    @ApiModelProperty(value = "是否上线")
    private Boolean isPublish;
}
