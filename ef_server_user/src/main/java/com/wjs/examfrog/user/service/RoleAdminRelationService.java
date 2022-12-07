package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.entity.RoleAdminRelation;

public interface RoleAdminRelationService extends IService<RoleAdminRelation> {
    RoleAdminRelation getOne(Long userId);
}
