package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FollowUser;
import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.FollowUserMapper;
import com.wjs.examfrog.user.service.FollowUserService;
import com.wjs.examfrog.user.service.UserService;
import com.wjs.examfrog.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FollowUserServiceImpl extends ServiceImpl<FollowUserMapper, FollowUser> implements FollowUserService {

    @Resource
    private UserService userService;


    @Override
    public Page listFollows(Long userId, PageParamDTO pageParamDTO) {
        Page<FollowUser> userPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 获取 关注的 用户id
        Page<FollowUser> followUserPage = this.page(userPage,new QueryWrapper<FollowUser>().eq("user_id", userId));
        List<Long> followIdList = followUserPage.getRecords().parallelStream()
                .map(FollowUser::getFollowId)
                .collect(Collectors.toList());

        // 如果没有就返回空数组
        if (followIdList.isEmpty()) {
            return new Page<UserVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(new ArrayList<>())
                    .setTotal(followUserPage.getTotal());
        }

        // 构建条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (Long followId : followIdList) {
            queryWrapper.eq("id", followId).or();
        }
        List<User> userList = userService.list(queryWrapper);

        return new Page<UserVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(userService.convertUserVOList(userList))
                .setTotal(followUserPage.getTotal());
    }

    @Override
    public Page listFans(Long userId, PageParamDTO pageParamDTO) {
        Page<FollowUser> followUserPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 获取 粉丝的 用户id
        Page<FollowUser> fansPage = this.page(followUserPage,new QueryWrapper<FollowUser>().eq("follow_id", userId));
        List<Long> fansIdList = fansPage.getRecords().parallelStream()
                .map(FollowUser::getUserId)
                .collect(Collectors.toList());

        // 如果没有就返回空数组
        if (fansIdList.isEmpty()) {
            return new Page<UserVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(new ArrayList<>())
                    .setTotal(fansPage.getTotal());
        }

        // 构建条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (Long fansId : fansIdList) {
            queryWrapper.eq("id", fansId).or();
        }
        List<User> userList = userService.list(queryWrapper);

        return new Page<UserVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(userService.convertUserVOList(userList))
                .setTotal(fansPage.getTotal());
    }

    @Override
    public void saveFollowUser(Long userId, Long followId) {
        User followDB = userService.getById(followId);
        if (followDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "关注的用户不存在");
        }

        // 维护 follow
        User follow = new User();
        follow.setId(followDB.getId());
        follow.setFansCount(followDB.getFansCount() + 1);
        userService.updateById(follow);

        FollowUser followUser = new FollowUser();
        followUser.setUserId(userId);
        followUser.setFollowId(followId);

        this.save(followUser);
    }

    @Override
    public void removeFollowUsers(Long userId, List<Long> followIdList) {
        HashSet<Long> followIdSet = new HashSet<>(followIdList);
        List<FollowUser> followUserList = this.list(new QueryWrapper<FollowUser>().eq("user_id", userId));
        // 获取符合要求的 FollowUserList
        List<Long> qualifiedFollowIdList = followUserList.parallelStream()
                .map(FollowUser::getFollowId)
                .filter(followIdSet::contains)
                .collect(Collectors.toList());

        // 维护 followList
        List<User> followList = userService.listByIds(qualifiedFollowIdList);
        for (User follow : followList) {
            follow.setFansCount(follow.getFansCount() - 1);
        }
        userService.updateBatchById(followList);

        // 获取符合要求的 idList
        List<Long> qualifiedFollowUserIdList = followUserList.parallelStream()
                .filter(x -> followIdSet.contains(x.getFollowId()))
                .map(FollowUser::getId)
                .collect(Collectors.toList());

        boolean res = this.removeByIds(qualifiedFollowUserIdList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "取关失败");
        }
    }

}
