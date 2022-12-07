package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskImage;
import com.wjs.examfrog.other.mapper.SubtaskImageMapper;
import com.wjs.examfrog.other.service.SubtaskImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SubtaskImageServiceImpl extends ServiceImpl<SubtaskImageMapper, SubtaskImage> implements SubtaskImageService {

    @Resource
    private SubtaskImageMapper subtaskImageMapper;

    @Override
    public Map<Long, List<UrlDTO>> getMapBySubtaskIdList(List<Long> subtaskIdList) {
        List<SubtaskImage> subtaskFileList = this.listSubtaskImagesBySubtaskIdList(subtaskIdList);
        Map<Long, List<UrlDTO>> res = new HashMap<>();
        for (SubtaskImage subtaskImage : subtaskFileList) {
            List<UrlDTO> urlDTOList = res.getOrDefault(subtaskImage.getSubtaskId(), new ArrayList<>());
            urlDTOList.add(this.convertUrlDTO(subtaskImage));
        }

        return res;
    }

    @Override
    public List<SubtaskImage> listSubtaskImagesBySubtaskIdList(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<SubtaskImage> queryWrapper = new QueryWrapper<>();
        for (Long subtaskId : subtaskIdList) {
            queryWrapper.eq("subtask_id", subtaskId).or();
        }

        return this.list(queryWrapper);
    }

    @Override
    public void saveSubtaskImages(Long subtaskId, List<UrlParamDTO> urlParamDTOList) {
        List<SubtaskImage> subtaskImageList = urlParamDTOList.parallelStream()
                .map(x -> {
                    SubtaskImage subtaskImage = new SubtaskImage();
                    BeanUtil.copyProperties(x, subtaskImage);
                    subtaskImage.setSubtaskId(subtaskId);
                    return subtaskImage;
                }).collect(Collectors.toList());

        this.saveBatch(subtaskImageList);
    }

    @Override
    public void saveSubtaskImages(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamImageUrlList) {
        List<SubtaskImage> subtaskImageList = new ArrayList<>();

        for (int i = 0; i < subtaskIdList.size(); i++) {
            Long subtaskId = subtaskIdList.get(i);
            List<UrlParamDTO> urlParamDTOList = subtaskParamImageUrlList.get(i);

            List<SubtaskImage> res = urlParamDTOList.parallelStream()
                    .map(x -> {
                        SubtaskImage subtaskImage = new SubtaskImage();
                        BeanUtil.copyProperties(x, subtaskImage);
                        subtaskImage.setSubtaskId(subtaskId);
                        return subtaskImage;
                    }).collect(Collectors.toList());

            subtaskImageList.addAll(res);
        }

        this.saveBatch(subtaskImageList);
    }

    @Override
    public void removeSubtaskImages(Long subtaskId) {
        this.remove(new QueryWrapper<SubtaskImage>().eq("subtask_id", subtaskId));
    }

    @Override
    public void removeSubtaskImages(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        QueryWrapper<SubtaskImage> queryWrapper = new QueryWrapper<>();
        subtaskIdList.forEach(x -> queryWrapper.eq("subtask_id", x).or());

        this.remove(queryWrapper);
    }

    @Override
    public void recoveryDeletedSubtaskImages(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskImageMapper.recoveryDeletedBySubtaskIdList(subtaskIdList);
    }

    @Override
    public void removeDeletedSubtaskImages(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskImageMapper.deleteDeletedBySubtaskIdList(subtaskIdList);
    }

    public UrlDTO convertUrlDTO(SubtaskImage subtaskImage) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setName(subtaskImage.getName());
        urlDTO.setUrl(subtaskImage.getUrl());
        return urlDTO;
    }

}
