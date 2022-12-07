package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.dto.PostManageDTO;
import com.wjs.examfrog.entity.UserPost;
import org.apache.ibatis.annotations.Param;

public interface UserPostMapper extends BaseMapper<UserPost> {
    /**
     * 后台管理 通过分区获取用户帖子
     * @param page
     * @return
     */
    Page<UserPost> listByManage(Page<UserPost> page);

    /**
     * 后台管理 上线用户帖子
     * @param userPostId
     * @return
     */
    Integer onlineUserPost(Long userPostId);

    /**
     * 后台管理 彻底删除用户帖子
     * @param userPostId
     * @return
     */
    Boolean deleteUserPostForManage(Long userPostId);

    /**
     * 后台管理 通过id获取用户帖子
     * @param userPostId
     * @return
     */
    UserPost getOneForManage(Long userPostId);

    /**
     * 后台管理 用户文章搜索青蛙分区
     * @param page
     * @param post
     * @return
     */
    Page<UserPost> searchCommonPost(Page<UserPost> page,@Param("post") PostManageDTO post,@Param("zoneId") Long zoneId);

    /**
     * 后台管理 用户文章搜索全部分区
     * @param page
     * @param post
     * @return
     */
    Page<UserPost> searchAllPost(Page<UserPost> page,@Param("post") PostManageDTO post);

    /**
     * 后台管理 页签管理统计数量
     * @param areaId
     * @param categoryId
     * @return
     */
    Long countNum(@Param("areaId") Long areaId,@Param("categoryId") Long categoryId);
}
