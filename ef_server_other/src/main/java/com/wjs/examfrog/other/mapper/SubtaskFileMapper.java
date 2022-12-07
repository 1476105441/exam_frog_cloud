package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.SubtaskFile;

import java.util.List;

public interface SubtaskFileMapper extends BaseMapper<SubtaskFile> {

    /**
     * 恢复 SubtaskFile
     * @param subtaskIdList subtask Id列表
     * @return 改变行数
     */
    int recoveryDeletedBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 删除 SubtaskFile
     * @param subtaskIdList 子任务 id列表
     * @return 改变行数
     */
    int deleteDeletedBySubtaskIdList(List<Long> subtaskIdList);

}
