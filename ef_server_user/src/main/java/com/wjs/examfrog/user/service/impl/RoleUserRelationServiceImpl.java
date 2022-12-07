package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.RoleUserRelation;
import com.wjs.examfrog.user.mapper.RoleUserRelationMapper;
import com.wjs.examfrog.user.service.RoleUserRelationService;
import org.springframework.stereotype.Service;

@Service
public class RoleUserRelationServiceImpl extends ServiceImpl<RoleUserRelationMapper, RoleUserRelation> implements RoleUserRelationService {

}
