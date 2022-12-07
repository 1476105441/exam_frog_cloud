package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FavUserPost;

import java.util.List;

public interface FavUserPostService extends IService<FavUserPost> {

    /**
     * 分页查询
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO);

    /**
     * 插入
     *
     * @param userId
     * @param userPostId
     */
    Boolean saveFavUserPost(Long userId, Long userPostId, Long favPostFolderId);

    /**
     * 删除
     *
     * @param userId
     * @param userPostIdList
     */
    Boolean removeFavUserPosts(Long userId, List<Long> userPostIdList);

}
