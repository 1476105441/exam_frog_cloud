package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskFile;

import java.util.List;
import java.util.Map;

public interface SubtaskFileService extends IService<SubtaskFile> {

    /**
     * 查询
     *
     * @param subtaskIdList
     * @return
     */
    Map<Long, List<UrlDTO>> getMapBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 查询
     *
     * @param subtaskIdList
     * @return
     */
    List<SubtaskFile> listSubtaskFilesBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 插入
     *
     * @param subtaskId
     * @param urlParamDTOList
     */
    void saveSubtaskFiles(Long subtaskId, List<UrlParamDTO> urlParamDTOList);

    /**
     * 插入
     *
     * @param subtaskIdList
     * @param subtaskParamFileUrlList
     */
    void saveSubtaskFiles(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamFileUrlList);

    /**
     * 删除
     *
     * @param subtaskId
     */
    void removeSubtaskFiles(Long subtaskId);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeSubtaskFiles(List<Long> subtaskIdList);

    /**
     * 恢复
     *
     * @param subtaskIdList
     */
    void recoveryDeletedSubtaskFiles(List<Long> subtaskIdList);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeDeletedSubtaskFiles(List<Long> subtaskIdList);

}
