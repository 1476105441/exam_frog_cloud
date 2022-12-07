package com.wjs.examfrog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台管理用到的帖子查询对象
 */
@Data
public class PostManageDTO {
    @ApiModelProperty(value = "文章标题", position = -98)
    private String title;

    @ApiModelProperty(value = "发布时间，使用 yyyy-MM-dd 格式的字符串，也可以是 yyyy(年) 或者 yyyy-MM(年月)")
    private String date;

    @ApiModelProperty(value = "文章状态 0是下线，1是上线，2是草稿，为空查询全部")
    private Long status;

    @ApiModelProperty(value = "排序 0是阅读数从高到低，1是阅读数从低到高，2是发布时间从近至远，3是发布时间从远至近")
    private Long orderType;

    @ApiModelProperty(value = "用户名称，官方帖子中置为空")
    private String userName;

    @ApiModelProperty(value = "页签id，为空查询全部")
    private Long category;

    @ApiModelProperty(value = "分区 0是青蛙乐园，1是牛蛙经验，为空查询全部，官方文章管理中置为空即可")
    private Long zone;
}
