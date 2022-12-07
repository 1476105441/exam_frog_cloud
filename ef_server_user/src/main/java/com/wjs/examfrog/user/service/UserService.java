package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.*;
import com.wjs.examfrog.entity.FavPostFolder;
import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.vo.*;

import java.util.List;

public interface UserService extends IService<User> {

    void insert(LoginParamDTO loginParamDTO);

    /**
     * 注册
     *
     * @param loginParamDTO
     * @return
     */
    UserLoginVO register(LoginParamDTO loginParamDTO);

    /**
     * 登录
     *
     * @param openId
     * @return
     */
    UserLoginVO login(String openId);

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    UserDetailsVO detail(Long id);

    /**
     * 获取
     *
     * @param openId
     * @return
     */
    User getUserByOpenId(String openId);

    /**
     * ===============================FollowUser=============================
     */
    /**
     * 分页
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFollows(Long userId, PageParamDTO pageParamDTO);

    /**
     * 分页
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

    /**
     * ========================UserPost=========================
     */
    /**
     * 发布
     *
     * @param userId
     * @param userPostParamDTO
     */
    void publishUserPost(Long userId, UserPostParamDTO userPostParamDTO);

    /**
     * 更新
     *
     * @param userId
     * @param userPostId
     * @param userPostParamDTO
     */
    void updateUserPost(Long userId, Long userPostId, UserPostParamDTO userPostParamDTO);

    /**
     * 删除
     *
     * @param userId
     * @param userPostIdList
     */
    void removeUserPosts(Long userId, List<Long> userPostIdList);
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



    /**
     * =======================FavUserPost======================
     */
    /**
     * 查询
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO);

    /**
     * 保存
     *
     * @param userId
     * @param userPostId
     */
    void saveFavUserPost(Long userId, Long userPostId, Long favPostFolderId);

    /**
     * 删除
     *
     * @param userId
     * @param userPostIdList
     */
    void removeFavUserPosts(Long userId, List<Long> userPostIdList);


    /**
     * ========================Subtask=========================
     */
    /**
     * 完成
     *
     * @param userId
     * @param planningId
     * @param subtaskId
     */
    void bingo(Long userId, Long planningId, Long subtaskId);

    /**
     * 取消完成
     *
     * @param userId
     * @param planningId
     * @param subtaskId
     */
    void cancelBingo(Long userId, Long planningId, Long subtaskId);

    /**
     * =======================PlanningFolder======================
     */
    /**
     * 分页
     *
     * @param userId
     * @param planningFolderId
     * @param pageParamDTO
     * @return
     */
    Page listPlanningFolders(Long userId, Long planningFolderId, PageParamDTO pageParamDTO);

    /**
     * 保存
     *
     * @param userId
     * @param planningFolderName
     * @param pid
     */
    void savePlanningFolder(Long userId, String planningFolderName, Long pid);

    /**
     * 修改
     *
     * @param userId
     * @param planningFolderId
     * @param planningFolderName
     */
    void updatePlanningFolder(Long userId, Long planningFolderId, String planningFolderName);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderIdList
     */
    void removePlanningFolders(Long userId, List<Long> planningFolderIdList);

    /**
     * 移动
     *
     * @param userId
     * @param planningFolderIdList
     * @param targetPlanningFolderId
     */
    void movePlanningFolders(Long userId, List<Long> planningFolderIdList, Long targetPlanningFolderId);

    /**
     * =======================Planning======================
     */
    /**
     * 查询
     *
     * @param userId
     * @param planningFolderId
     * @param planningId
     * @return
     */
    PlanningDetailsVO getPlanningDetail(Long userId, Long planningFolderId, Long planningId);

    /**
     * 分页
     *
     * @param userId
     * @param planningFolderId
     * @param pageParamDTO
     * @return
     */
    Page listPlannings(Long userId, Long planningFolderId, PageParamDTO pageParamDTO);

    /**
     * 保存
     *
     * @param userId
     * @param planningFolderId
     * @param planningParamDTO
     */
    void savePlanning(Long userId, Long planningFolderId, PlanningParamDTO planningParamDTO);

    /**
     * 修改
     *
     * @param userId
     * @param planningFolderId
     * @param planningId
     * @param planningParamDTO
     */
    void updatePlanning(Long userId, Long planningFolderId, Long planningId, PlanningParamDTO planningParamDTO);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderId
     * @param planningList
     */
    void removePlannings(Long userId, Long planningFolderId, List<Long> planningList);

    /**
     * 移动
     *
     * @param userId
     * @param planningFolderId
     * @param planningIdList
     * @param targetPlanningFolderId
     */
    void movePlannings(Long userId, Long planningFolderId, List<Long> planningIdList, Long targetPlanningFolderId);

    /**
     * =======================回收站==============================
     */
    /**
     * 分页
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listDeletedPlanningFolders(Long userId, PageParamDTO pageParamDTO);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderIdList
     */
    void recoveryDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderIdList
     */
    void removeDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList);

    /**
     * 查询
     *
     * @param userId
     * @param pageParamDTO
     * @return
     */
    Page listDeletedPlannings(Long userId, PageParamDTO pageParamDTO);

    /**
     * 恢复
     *
     * @param userId
     * @param userPostIdList
     */
    void recoveryDeletedPlannings(Long userId, List<Long> userPostIdList);

    /**
     * 删除
     *
     * @param userId
     * @param userPostIdList
     */
    void removeDeletedUserPosts(Long userId, List<Long> userPostIdList);

    /**
     * 清空
     *
     * @param userId
     */
    void removeAllDeleted(Long userId);

    /**
     * 转化
     *
     * @param userList
     * @return
     */
    List<UserVO> convertUserVOList(List<User> userList);

    /**
     * =======================收藏帖子文件夹==============================
     */

    /**
     * 新建文件夹
     * @param userId
     */
    void saveFavPostFolder(Long userId, FavPostFolder favPostFolder);

    /**
     * 获取文件夹
     * @param userId
     * @param favPostFolderId
     * @param pageParamDTO
     * @return
     */
    Page listFavPostFolders(Long userId, Long favPostFolderId, Long visitedId, PageParamDTO pageParamDTO);

    /**
     * 更新文件夹
     * @param userId
     * @param favPostFolderId
     * @param favPostFolder
     */
    void updateFavPostFolder(Long userId, FavPostFolder favPostFolder, Long favPostFolderId);

    /**
     * 移动文件夹
     * @param userId
     * @param favPostFolderIdList
     * @param targetId
     */
    void moveFavPostFolder(Long userId, List<Long> favPostFolderIdList, Long targetId);

    void removeFavPostFolder(Long userId, List<Long> favPostFolderIdList);

    /**
     * =======================后台管理 用户列表==============================
     */

    /**
     * 后台管理 查询用户列表
     * @param pageParamDTO
     * @return
     */
    Page<UserVO> listForManage(PageParamDTO pageParamDTO);

    /**
     * 后台管理 查询用户列表
     * @param pageParamDTO
     * @param userParamDTO
     * @return
     */
    Page<UserVO> listBySearch(PageParamDTO pageParamDTO,UserParamDTO userParamDTO);

    /**
     * 后台管理 冻结/解冻用户
     * @param idList
     * @param type
     */
    void freezeUser(List<Long> idList,Long type);

    Boolean checkOperator(Long userId);

    UserLoginVO adminLogin(AdminDTO adminDTO);
}
