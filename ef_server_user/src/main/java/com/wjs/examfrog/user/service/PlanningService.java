package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PlanningParamDTO;
import com.wjs.examfrog.entity.Planning;
import com.wjs.examfrog.vo.PlanningDetailsVO;

import java.util.List;


public interface PlanningService extends IService<Planning> {

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
     * @param planningId
     * @param planningParamDTO
     */
    void updatePlanning(Long userId, Long planningFolderId, Long planningId, PlanningParamDTO planningParamDTO);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderId
     * @param planningIdList
     */
    void removePlannings(Long userId, Long planningFolderId, List<Long> planningIdList);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderIdList
     */
    void removePlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList);

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
     * ==============================Subtask============================
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
     * =============================回收站============================
     */
    /**
     * 分页
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
     * @param planningIdList
     */
    void recoveryDeletedPlanningsByPlanningIdList(Long userId, List<Long> planningIdList);

    /**
     * 删除
     *
     * @param userId
     * @param planningIdList
     */
    void removeDeletedPlanningsByPlanningIdList(Long userId, List<Long> planningIdList);

    /**
     * 恢复
     *
     * @param userId
     * @param planningFolderIdList
     */
    void recoveryDeletedPlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList);

    /**
     * 删除
     *
     * @param userId
     * @param planningFolderIdList
     */
    void removeDeletedPlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList);

    /**
     * 清空
     *
     * @param userId
     */
    void removeAllDeletedPlanning(Long userId);

}
