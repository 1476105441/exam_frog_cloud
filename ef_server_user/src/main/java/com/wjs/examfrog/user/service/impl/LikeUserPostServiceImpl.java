package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.entity.LikeUserPost;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.LikeUserPostMapper;
import com.wjs.examfrog.user.service.LikeUserPostService;
import com.wjs.examfrog.user.service.ApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LikeUserPostServiceImpl extends ServiceImpl<LikeUserPostMapper, LikeUserPost> implements LikeUserPostService {

    @Resource
    private ApiService apiService;

    @Override
    public void saveLikeUserPost(Long userId, Long userPostId) {
        // 查看点赞的 userPost 是否存在
        UserPost userPostDB = apiService.getPostById(userPostId).getBody().getData();
        if (userPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "点赞的帖子不存在");
        }

        LikeUserPost likeUserPostDB = this.getOne(new QueryWrapper<LikeUserPost>()
                .eq("user_id", userId)
                .eq("user_post_id", userPostId));

        if (likeUserPostDB != null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "已存在");
        }

        // 维护一下帖子点赞数
        UserPost userPost = new UserPost();
        userPost.setId(userPostDB.getId());
        userPost.setLikeCount(userPostDB.getLikeCount() + 1);
        if (!apiService.updateOneById(userPost).getBody().getData()) {
            throw new ApiException("点赞失败");
        }

        // 添加
        LikeUserPost likeUserPost = new LikeUserPost();
        likeUserPost.setUserId(userId);
        likeUserPost.setUserPostId(userPostId);

        boolean res = this.save(likeUserPost);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "添加失败");
        }
    }

    @Override
    public void removeLikeUserPost(Long userId, Long userPostId) {
        // 查看点赞的 userPost 是否存在
        UserPost userPostDB = apiService.getPostById(userPostId).getBody().getData();
        if (userPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "取消点赞的帖子不存在");
        }

        LikeUserPost likeUserPostDB = this.getOne(new QueryWrapper<LikeUserPost>()
                .eq("user_id", userId)
                .eq("user_post_id", userPostId));
        if (likeUserPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "点赞关系不存在");
        }

        // 维护一下帖子点赞数
        UserPost userPost = new UserPost();
        userPost.setId(userPostDB.getId());
        userPost.setLikeCount(userPostDB.getLikeCount() - 1);
        if (!apiService.updateOneById(userPost).getBody().getData()) {
            throw new ApiException("点赞失败");
        }

        // 删除
        boolean res = this.removeById(likeUserPostDB);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "添加失败");
        }
    }
}
