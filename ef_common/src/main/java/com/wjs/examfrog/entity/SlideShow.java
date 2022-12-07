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
@ApiModel(value = "轮播图对象")
public class SlideShow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("分区名称")
    private String kind;

    @ApiModelProperty("跳转地址")
    private String skipUrl;

    @ApiModelProperty("当前状态，1-上线 0-下线")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableLogic
    private Boolean isDelete;

    @Version
    private Long version;
}
