package com.wjs.examfrog.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserPost对象", description="")
public class UserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    private String content;

    private String tags;

    private Boolean isOriginal;

    private Boolean isAuthentic;

    private Long tryingCount;

    private Long successCount;

    private String titleImageUrl;

    private Long categoryId;

    private Long authorId;

    private String authorNickName;

    private String authorAvatarUrl;

    private Long likeCount;

    private Long favCount;

    private Long viewCount;

    private Long shareCount;

    private Long commentCount;

    private Long areaId;

    private Boolean completed;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableLogic
    private Boolean isDelete;

    @Version
    private Long version;


}
