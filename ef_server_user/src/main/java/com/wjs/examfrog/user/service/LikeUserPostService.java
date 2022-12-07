package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.entity.LikeUserPost;

public interface LikeUserPostService extends IService<LikeUserPost> {

    /**
     * 插入
     *
     * @param userId
     * @param userPostId
     */
    void saveLikeUserPost(Long userId, Long userPostId);

    /**
     * 删除
     *
     * @param userId
     * @param userPostId
     */
    void removeLikeUserPost(Long userId, Long userPostId);

}
