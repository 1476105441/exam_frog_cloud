package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskImage;

import java.util.List;
import java.util.Map;

public interface SubtaskImageService extends IService<SubtaskImage> {

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
    List<SubtaskImage> listSubtaskImagesBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 插入
     *
     * @param subtaskId
     * @param urlParamDTOList
     */
    void saveSubtaskImages(Long subtaskId, List<UrlParamDTO> urlParamDTOList);

    /**
     * 插入
     *
     * @param subtaskIdList
     * @param subtaskParamImageUrlList
     */
    void saveSubtaskImages(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamImageUrlList);

    /**
     * 删除
     *
     * @param subtaskId
     */
    void removeSubtaskImages(Long subtaskId);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeSubtaskImages(List<Long> subtaskIdList);

    /**
     * 恢复
     *
     * @param subtaskIdList
     */
    void recoveryDeletedSubtaskImages(List<Long> subtaskIdList);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeDeletedSubtaskImages(List<Long> subtaskIdList);

}
