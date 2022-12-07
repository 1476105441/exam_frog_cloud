package com.wjs.examfrog.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.entity.PlanningFolder;
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
public interface PlanningFolderMapper extends BaseMapper<PlanningFolder> {

    /**
     * 根据 userId 查询已经删除的所有规划文件夹
     * @param userId 用户id
     * @return 已删除的所有规划文件夹
     */
    Page<PlanningFolder> selectDeletedPlanningFolders(Page<PlanningFolder> page, @Param("userId") Long userId);

    /**
     * 根据 userId 和 planningFolderIdList 恢复已经删除的规划文件夹
     * @param userId 用户id
     * @param planningFolderIdList planningFolder id列表
     * @return 修改行数
     */
    int recoveryDeletedPlanningFolders(@Param("userId") Long userId, @Param("planningFolderIdList") List<Long> planningFolderIdList);

    /**
     * 根据 userId 和 planningFolderIdList 删除已经删除的规划文件夹
     * @param userId 用户id
     * @param planningFolderIdList planningFolder id列表
     * @return 修改行数
     */
    int deleteDeletedPlanningFolders(@Param("userId") Long userId, @Param("planningFolderIdList") List<Long> planningFolderIdList);

    /**
     * 根据 userId 查询已经删除的所有规划文件夹
     * @param userId 用户id
     * @return 修改行数
     */
    int deleteAllDeletedPlanningFolders(Long userId);

}
