package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.Role;
import com.wjs.examfrog.entity.RoleUserRelation;
import com.wjs.examfrog.user.mapper.RoleMapper;
import com.wjs.examfrog.user.service.RoleService;
import com.wjs.examfrog.user.service.RoleUserRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleUserRelationService roleUserRelationService;

    /**
     * 2次单表查询:
     * 1. ret = user role_user_relation
     * 2. ret = ret role
     */
    @Override
    public List<Role> listRolesByUserId(Long userId) {
        List<RoleUserRelation> roleUserRelationsList =
                roleUserRelationService.list(new QueryWrapper<RoleUserRelation>().eq("user_id", userId));

        if (roleUserRelationsList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        roleUserRelationsList.forEach(x -> queryWrapper.eq("id", x.getRoleId()).or());

        return this.list(queryWrapper);
    }

}
