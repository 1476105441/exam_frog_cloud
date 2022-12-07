package com.wjs.examfrog.other.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FavUserPost;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.mapper.FavUserPostMapper;
import com.wjs.examfrog.other.service.FavUserPostService;
import com.wjs.examfrog.other.service.UserPostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavUserPostServiceImpl extends ServiceImpl<FavUserPostMapper, FavUserPost> implements FavUserPostService {

    @Resource
    private UserPostService userPostService;

    @Override
    public Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO) {
        return userPostService.listFavUserPosts(userId, pageParamDTO);
    }

    @Override
    public Boolean saveFavUserPost(Long userId, Long userPostId, Long favPostFolderId) {
        // 查看收藏的 userPost 是否存在
        UserPost userPostDB = userPostService.getById(userPostId);
        if (userPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "收藏的帖子不存在");
        }

        FavUserPost collectionDB = this.getOne(new QueryWrapper<FavUserPost>()
                .eq("user_id", userId)
                .eq("user_post_id", userPostId));
        if (collectionDB != null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "已存在");
        }

        // 维护一下帖子收藏数
        UserPost userPost = new UserPost();
        userPost.setId(userPostDB.getId());
        userPost.setFavCount(userPostDB.getFavCount() + 1);
        userPostService.updateById(userPost);

        // 添加
        FavUserPost favUserPost = new FavUserPost();
        favUserPost.setUserId(userId);
        favUserPost.setUserPostId(userPostId);
        favUserPost.setFavPostFolderId(favPostFolderId);

        return this.save(favUserPost);
    }

    @Override
    public Boolean removeFavUserPosts(Long userId, List<Long> userPostIdList) {
        HashSet<Long> userPostIdSet = new HashSet<>(userPostIdList);
        List<FavUserPost> favUserPostList = this.list(new QueryWrapper<FavUserPost>().eq("user_id", userId));
        // 获取符合要求的 userPostList
        List<Long> qualifiedUserPostIdList = favUserPostList.parallelStream()
                .map(FavUserPost::getUserPostId) // 映射 只取UserPostId
                .filter(userPostIdSet::contains) // 集合中没有的都过滤掉
                .collect(Collectors.toList());

        // 维护 UserPostList
        List<UserPost> userPostList = userPostService.listByIds(qualifiedUserPostIdList);
        for (UserPost userPost : userPostList) {
            // 每个帖子的收藏数减1
            userPost.setFavCount(userPost.getFavCount() - 1);
        }
        userPostService.updateBatchById(userPostList);

        // 获取符合要求的 idList
        List<Long> qualifiedFavUserPostIdList = favUserPostList.parallelStream()
                .filter(x -> userPostIdSet.contains(x.getUserPostId()))
                .map(FavUserPost::getId) // FavUserPost的id 不是UserPostId
                .collect(Collectors.toList());

        return this.removeByIds(qualifiedFavUserPostIdList);
    }

}
