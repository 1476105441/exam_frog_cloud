package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.SubtaskImage;

import java.util.List;


public interface SubtaskImageMapper extends BaseMapper<SubtaskImage> {

    /**
     * 恢复 SubtaskImage
     * @param subtaskIdList subtask Id列表
     * @return 改变行数
     */
    int recoveryDeletedBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 删除 SubtaskImage
     * @param subtaskIdList subtask Id列表
     * @return 改变行数
     */
    int deleteDeletedBySubtaskIdList(List<Long> subtaskIdList);

}
