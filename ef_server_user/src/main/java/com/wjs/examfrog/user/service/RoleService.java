package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    /**
     * 查询
     * @param userId
     * @return
     */
    List<Role> listRolesByUserId(Long userId);

}
