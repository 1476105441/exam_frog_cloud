package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.RoleAdminRelation;
import com.wjs.examfrog.user.mapper.RoleAdminRelationMapper;
import com.wjs.examfrog.user.service.RoleAdminRelationService;
import org.springframework.stereotype.Service;

@Service
public class RoleAdminRelationServiceImpl extends ServiceImpl<RoleAdminRelationMapper, RoleAdminRelation> implements RoleAdminRelationService {
    @Override
    public RoleAdminRelation getOne(Long userId) {
        QueryWrapper<RoleAdminRelation> wrapper = new QueryWrapper<RoleAdminRelation>().eq("admin_id", userId);
        return this.getOne(wrapper);
    }
}
