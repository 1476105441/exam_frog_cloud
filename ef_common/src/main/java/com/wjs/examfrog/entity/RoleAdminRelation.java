package com.wjs.examfrog.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RoleAdminRelation对象", description="")
public class RoleAdminRelation {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long roleId;

    private Long adminId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableLogic
    private Boolean isDelete;

    @Version
    private Long version;
}
