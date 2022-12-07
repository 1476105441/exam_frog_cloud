package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PostManageDTO;
import com.wjs.examfrog.dto.UserPostParamDTO;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.vo.UserPostDetailsVO;
import com.wjs.examfrog.vo.UserPostManageDetailsVO;
import com.wjs.examfrog.vo.UserPostVO;

import java.util.List;

public interface UserPostService extends IService<UserPost> {

    /**
     * 查询全部
     *
     * @return
     */
    List<UserPostVO> listAllUserPosts();

    /**
     * 分页
     *
     * @param pageParamDTO
     * @param words
     * @return
     */
    Page listUserPostsByQuery(PageParamDTO pageParamDTO, String words);

    /**
     * @param categoryIdList
     * @param pageParamDTO
     * @return
     */
    Page listUserPosts(List<Long> categoryIdList, PageParamDTO pageParamDTO);

    /**
     * 分页
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listUserPostsByUserId(Long userId, PageParamDTO pageParamDTO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    UserPostDetailsVO detail(Long id);

    /**
     * 发布
     *
     * @param userId
     * @param userPostParamDTO
     */
    Boolean publishUserPost(Long userId, UserPostParamDTO userPostParamDTO);

    /**
     * 更新
     *
     * @param userId
     * @param userPostId
     * @param userPostParamDTO
     */
    Boolean updateUserPost(Long userId, Long userPostId, UserPostParamDTO userPostParamDTO);

    /**
     * 删除
     *
     * @param userId
     * @param userPostIdList
     */
    Boolean removeUserPosts(Long userId, List<Long> userPostIdList);

    /**
     * 获取榜单
     *
     * @param size
     * @return
     */
    List<UserPostVO> getHotList(Long size);

    /**
     * 分页
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO);

    /**
     * 计数++
     *
     * @param userPostId
     */
    Boolean increaseSuccessCount(Long userPostId);

    /**
     * 查询个人帖子
     * @param pageParamDTO
     * @param userId
     * @param orderType
     * @return
     */
    Page listUserPostById(PageParamDTO pageParamDTO,Long userId,int orderType);

    /**
     * 后台管理 管理用户帖子
     * @param pageParamDTO
     * @return
     */
    Page listByManage(PageParamDTO pageParamDTO);

    /**
     * 后台管理 查询用户帖子
     * @param postId
     * @return
     */
    UserPostManageDetailsVO manageDetail(Long postId);

    /**
     * 后台管理 上线用户帖子
     * @param userPostId
     */
    void onlineUserPost(Long userId,Long userPostId);

    /**
     * 后台管理 下线用户帖子
     * @param userId
     * @param userPostId
     */
    void removeUserPostForManage(Long userId,Long userPostId);

    /**
     * 后台管理 删除用户帖子
     * @param userId
     * @param userPostId
     */
    void deleteUserPostForManage(Long userId,Long userPostId);

    /**
     * 后台管理 搜索用户帖子
     * @param pageParamDTO
     * @param postManageDTO
     * @return
     */
    Page listBySearch(PageParamDTO pageParamDTO, PostManageDTO postManageDTO);

    /**
     * 检查PostManageDTO是否符合要求
     * @param postManageDTO
     */
    void checkPostManageDTO(PostManageDTO postManageDTO);

    /**
     * 后台管理 牛蛙内容
     * @param pageParamDTO
     * @return
     */
    Page listExcellentByManage(PageParamDTO pageParamDTO);

    /**
     * 后台管理 操作牛蛙内容
     */
    void controlStatus(Long userId,Long id,Long type);

    /**
     * 转换userPost
     * @param userPost
     * @return
     */
    UserPostVO convertUserPostVO(UserPost userPost);

    List<UserPost> listUserPostByUserId(Long userId);

    UserPost getOneById(Long userPostId);

    Boolean updateOneById(UserPost userPost);
}
