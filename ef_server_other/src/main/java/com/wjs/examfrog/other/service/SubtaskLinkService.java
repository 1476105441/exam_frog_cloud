package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskLink;

import java.util.List;
import java.util.Map;

public interface SubtaskLinkService extends IService<SubtaskLink> {

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
    List<SubtaskLink> listSubtaskLinksBySubtaskIdList(List<Long> subtaskIdList);

    /**
     * 插入
     *
     * @param subtaskId
     * @param urlParamDTOList
     */
    void saveSubtaskLinks(Long subtaskId, List<UrlParamDTO> urlParamDTOList);

    /**
     * 插入
     *
     * @param subtaskIdList
     * @param subtaskParamLinkUrlList
     */
    void saveSubtaskLinks(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamLinkUrlList);

    /**
     * 删除
     *
     * @param subtaskId
     */
    void removeSubtaskLinks(Long subtaskId);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeSubtaskLinks(List<Long> subtaskIdList);

    /**
     * 恢复
     *
     * @param subtaskIdList
     */
    void recoveryDeletedSubtaskLinks(List<Long> subtaskIdList);

    /**
     * 删除
     *
     * @param subtaskIdList
     */
    void removeDeletedSubtaskLinks(List<Long> subtaskIdList);

}
