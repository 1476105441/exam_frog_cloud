package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.Subtask;

import java.util.List;

public interface SubtaskMapper extends BaseMapper<Subtask> {

    /**
     * 根据 planningIdList, 查询 Subtask Id列表
     * @param planningIdList 规划 id列表
     * @return Subtask Id列表
     */
    List<Long> selectDeletedSubtaskIdListByPlanningIdList(List<Long> planningIdList);

    /**
     * 恢复 Subtask
     * @param planningIdList 规划 id列表
     * @return 改变行数
     */
    Boolean recoveryDeletedSubtasksByPlanningIdList(List<Long> planningIdList);

    /**
     * 恢复 Subtask
     * @param planningIdList 规划 id列表
     * @return 改变行数
     */
    Boolean deleteDeletedSubtasksByPlanningIdList(List<Long> planningIdList);

//    List<Long> selectDeletedSubtasksSubtaskIdListByUserPostIdList(List<Long> userPostIdList);
//
//    int recoveryDeletedSubtasksByUserPostIdList(List<Long> userPostIdList);
//
//    int deleteDeletedSubtasksByUserPostIdList(List<Long> userPostIdList);

}
