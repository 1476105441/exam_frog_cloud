package com.wjs.examfrog.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="帖子收藏夹对象", description="")
public class FavPostFolder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id", required = true, hidden = true)
    private Long userId;

    @ApiModelProperty(value = "文件夹名称", required = true)
    private String name;

    @ApiModelProperty(value = "父文件夹id", required = true)
    private Long pid;

    @ApiModelProperty(value = "文件夹是否公开", required = true)
    private Boolean isOpen;

    @ApiModelProperty(value = "文件夹描述", required = true)
    private String description;

    @ApiModelProperty(value = "添加时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更改时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "逻辑删除", hidden = true)
    @TableLogic
    private Boolean isDelete;

    @ApiModelProperty(value = "乐观锁", required = false, hidden = true)
    @Version
    private Long version;


}
