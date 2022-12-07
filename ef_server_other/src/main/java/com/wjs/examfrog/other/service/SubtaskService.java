package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.SubtaskParamDTO;
import com.wjs.examfrog.entity.Subtask;

import java.util.List;

public interface SubtaskService extends IService<Subtask> {

    /**
     * 查询
     *
     * @param userPostId
     * @return
     */
    List<SubtaskDTO> listSubtasksByUserPostId(Long userPostId);

    /**
     * 插入
     *
     * @param userPostId
     * @param subtaskParamDTOList
     */
    void saveSubtasksByUserPostId(Long userPostId, List<SubtaskParamDTO> subtaskParamDTOList);

    /**
     * 修改
     *
     * @param userPostId
     * @param subtaskParamDTOList
     */
    void updateSubtasksByUserPostId(Long userPostId, List<SubtaskParamDTO> subtaskParamDTOList);

    /**
     * 删除
     *
     * @param userPostId
     */
    void removeSubtasksByUserPostId(Long userPostId);

    /**
     * 删除
     *
     * @param userPostIdList
     */
    void removeSubtasksByUserPostId(List<Long> userPostIdList);


    /**
     * 查询
     *
     * @param planningId
     * @return
     */
    List<SubtaskDTO> listSubtasksByPlanningId(Long planningId);

    /**
     * 插入
     *
     * @param planningId
     * @param subtaskParamDTOList
     */
    Boolean saveSubtasksByPlanningId(Long planningId, List<SubtaskParamDTO> subtaskParamDTOList);

    /**
     * 修改
     *
     * @param planningId
     * @param subtaskParamDTOList
     */
    Boolean updateSubtasksByPlanningId(Long planningId, List<SubtaskParamDTO> subtaskParamDTOList);

    /**
     * 删除
     *
     * @param planningId
     */
    void removeSubtasksByPlanningId(Long planningId);

    /**
     * 删除
     *
     * @param planningIdList
     */
    Boolean removeSubtasksByPlanningIdList(List<Long> planningIdList);

    /**
     * 计数
     *
     * @param planningId
     * @return
     */
    Long countFinishSubtaskCount(Long planningId);

    /**
     * 完成
     *
     * @param userPostId
     * @param subtaskId
     */
    Boolean bingo(Long userPostId, Long subtaskId);

    /**
     * 取消完成
     *
     * @param userPostId
     * @param subtaskId
     */
    Boolean cancelBingo(Long userPostId, Long subtaskId);

    /**
     * 恢复
     *
     * @param planningIdList
     */
    Boolean recoveryDeletedSubtasks(List<Long> planningIdList);

    /**
     * 删除
     *
     * @param planningIdList
     */
    Boolean removeDeletedSubtasks(List<Long> planningIdList);

}
