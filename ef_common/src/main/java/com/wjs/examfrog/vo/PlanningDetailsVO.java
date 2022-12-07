package com.wjs.examfrog.vo;

import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.UrlDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanningDetailsVO implements Serializable {

    /**
     * swagger: 置顶
     */
    @ApiModelProperty(value = "用户帖子id", position = -99)
    private Long id;
    @ApiModelProperty(value = "用户帖子标题", position = -98)
    private String title;
    @ApiModelProperty(value = "作者id", position = -97)
    private Long authorId;
    @ApiModelProperty(value = "分类id",  position = -96)
    private String categoryId;
    @ApiModelProperty(value = "文件夹id(-1 表示没有进入文件夹)",  position = -95)
    private Long planningFolderId;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "分类名称",  position = -94)
    private String categoryName;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "作者nickName", position = -94)
    private String authorNickName;
    @ApiModelProperty(value = "标签",  position = -93)
    private String tags;
    @ApiModelProperty(value = "采用的 UserPost的 Id (原创则 -1)",  position = -92)
    private Long followUserPostId;
    @ApiModelProperty(value = "最后修改时间",  position = -91)
    private LocalDateTime gmtModified;

    /**
     * swagger: 默认
     */
    @ApiModelProperty(value = "用户帖子内容")
    private String content;
    @ApiModelProperty(value = "头图url")
    private String titleImageUrl;
    @ApiModelProperty(value = "总目标数目")
    private Long totalSubtaskCount;
    @ApiModelProperty(value = "已完成数目")
    private Long finishSubtaskCount;

    /**
     * swagger: 置底
     */
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "链接数组", position = 100)
    private List<UrlDTO> linkUrlList;
    /**
     * 联表查询
     */
    @ApiModelProperty(value = "目标数组 (已按 idx 排序)", position = 101)
    private List<SubtaskDTO> subtaskList;

}
