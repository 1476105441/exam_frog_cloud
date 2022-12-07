package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.SubtaskLink;

import java.util.List;

public interface SubtaskLinkMapper extends BaseMapper<SubtaskLink> {

    /**
     * 恢复 SubtaskLink
     * @param subtaskIdList subtask Id列表
     * @return 改变行数
     */
    int recoveryDeletedBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 删除 SubtaskLink
     * @param subtaskIdList subtask Id列表
     * @return 改变行数
     */
    int deleteDeletedBySubtaskIdList(List<Long> subtaskIdList);

}
