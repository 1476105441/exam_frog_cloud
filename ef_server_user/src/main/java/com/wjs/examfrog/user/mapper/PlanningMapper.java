package com.wjs.examfrog.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.entity.Planning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dingdong
 * @since 2021-08-12
 */
public interface PlanningMapper extends BaseMapper<Planning> {

    /**
     * 根据 userId 查询已经删除的所有规划
     * @param userId 用户id
     * @return 已删除的所有规划
     */
    Page<Planning> selectDeletedPlannings(Page<Planning> page,@Param("userId") Long userId);

    /**
     * 根据 userId 和 planningFolderIdList 恢复已经删除的规划
     * @param userId 用户id
     * @param planningIdList planning id列表
     * @return 修改行数
     */
    int recoveryDeletedPlannings(@Param("userId") Long userId, @Param("planningIdList") List<Long> planningIdList);

    /**
     * 根据 userId 和 planningIdList 删除已经删除的规划
     * @param userId 用户id
     * @param planningIdList planning id列表
     * @return 修改行数
     */
    int deleteDeletedPlannings(@Param("userId") Long userId, @Param("planningIdList") List<Long> planningIdList);

    /**
     * 根据 userId 查询已经删除的所有规划
     * @param userId 用户id
     * @return 修改行数
     */
    int deleteAllDeletedPlannings(Long userId);

    /**
     * 根据 userId 和 planningFolderIdList, 查询 已删除的 Planning id列表
     * @param userId 用户id
     * @param planningFolderIdList planningFolder id列表
     * @return 已删除的 Planning id列表
     */
    List<Long> selectDeletedPlanningIdListByPlanningFolderIdList(@Param("userId") Long userId, @Param("planningFolderIdList") List<Long> planningFolderIdList);

    /**
     * 根据 userId, 查询 已删除的 Planning id列表
     * @param userId 用户id
     * @return 已删除的 Planning id列表
     */
    List<Long> selectDeletedPlanningIdListByUserId(Long userId);

}
