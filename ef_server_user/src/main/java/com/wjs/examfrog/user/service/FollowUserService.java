package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FollowUser;

import java.util.List;


public interface FollowUserService extends IService<FollowUser> {

    /**
     * 分页查询
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFollows(Long userId, PageParamDTO pageParamDTO);

    /**
     * 分页查询
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFans(Long userId, PageParamDTO pageParamDTO);

    /**
     * 插入
     *
     * @param userId
     * @param followId
     */
    void saveFollowUser(Long userId, Long followId);

    /**
     * 删除
     *
     * @param userId
     * @param followIdList
     */
    void removeFollowUsers(Long userId, List<Long> followIdList);

}
