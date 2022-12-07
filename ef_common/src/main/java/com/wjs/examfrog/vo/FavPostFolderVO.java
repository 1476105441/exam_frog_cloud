package com.wjs.examfrog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FavPostFolderVO implements Serializable {

    /**
     * swagger置顶
     */
    @ApiModelProperty(value = "用户文件夹id", position = -99)
    private Long id;

    @ApiModelProperty(value = "用户id", position = -98)
    private Long userId;

    @ApiModelProperty(value = "用户文件夹名称", position = -97)
    private String favPostFolderName;

    @ApiModelProperty(value = "文件夹描述")
    private String description;

    @ApiModelProperty(value = "文件夹是否公开")
    private Boolean isOpen;

    @ApiModelProperty(value = "最后修改时间", position = -96)
    private LocalDateTime gmtModified;

}
