package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PlanningParamDTO;
import com.wjs.examfrog.entity.PlanningFolder;
import com.wjs.examfrog.vo.PlanningDetailsVO;

import java.util.List;


public interface PlanningFolderService extends IService<PlanningFolder> {

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
     * 插入
     *
     * @param userId
     * @param planningFolderName
     * @param pid
     */
    void savePlanningFolder(Long userId, String planningFolderName, Long pid);

    /**
     * 更新
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
     * ==========================Planning=================================
     */
    /**
     * 详情
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
     * 插入
     *
     * @param userId
     * @param planningFolderId
     * @param planningParamDTO
     */
    void savePlanning(Long userId, Long planningFolderId, PlanningParamDTO planningParamDTO);

    /**
     * 更新
     *
     * @param userId
     * @param planningFolderId
     * @param userPostId
     * @param planningParamDTO
     */
    void updatePlanning(Long userId, Long planningFolderId, Long userPostId, PlanningParamDTO planningParamDTO);

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
     * @param userPostIdList
     * @param targetPlanningFolderId
     */
    void movePlannings(Long userId, Long planningFolderId, List<Long> userPostIdList, Long targetPlanningFolderId);

    /**
     * =============================回收站===========================
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
     * 恢复
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
     * 清空
     *
     * @param userId
     */
    void removeAllDeletedPlanningFolders(Long userId);

}
