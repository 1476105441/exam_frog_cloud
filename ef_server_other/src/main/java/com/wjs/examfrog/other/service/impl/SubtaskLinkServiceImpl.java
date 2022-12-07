package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskLink;
import com.wjs.examfrog.other.mapper.SubtaskLinkMapper;
import com.wjs.examfrog.other.service.SubtaskLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubtaskLinkServiceImpl extends ServiceImpl<SubtaskLinkMapper, SubtaskLink> implements SubtaskLinkService {

    @Resource
    private SubtaskLinkMapper subtaskLinkMapper;

    @Override
    public Map<Long, List<UrlDTO>> getMapBySubtaskIdList(List<Long> subtaskIdList) {
        List<SubtaskLink> subtaskFileList = this.listSubtaskLinksBySubtaskIdList(subtaskIdList);
        Map<Long, List<UrlDTO>> res = new HashMap<>();
        for (SubtaskLink subtaskLink : subtaskFileList) {
            List<UrlDTO> urlDTOList = res.getOrDefault(subtaskLink.getSubtaskId(), new ArrayList<>());
            urlDTOList.add(this.convertUrlDTO(subtaskLink));
        }

        return res;
    }

    @Override
    public List<SubtaskLink> listSubtaskLinksBySubtaskIdList(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<SubtaskLink> queryWrapper = new QueryWrapper<>();
        for (Long subtaskId : subtaskIdList) {
            queryWrapper.eq("subtask_id", subtaskId).or();
        }

        return this.list(queryWrapper);
    }

    @Override
    public void saveSubtaskLinks(Long subtaskId, List<UrlParamDTO> urlParamDTOList) {
        List<SubtaskLink> subtaskLinkList = urlParamDTOList.parallelStream()
                .map(x -> {
                    SubtaskLink subtaskLink = new SubtaskLink();
                    BeanUtil.copyProperties(x, subtaskLink);
                    subtaskLink.setSubtaskId(subtaskId);
                    return subtaskLink;
                }).collect(Collectors.toList());

        this.saveBatch(subtaskLinkList);
    }

    @Override
    public void saveSubtaskLinks(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamLinkUrlList) {
        List<SubtaskLink> subtaskLinkList = new ArrayList<>();

        for (int i = 0; i < subtaskIdList.size(); i++) {
            Long subtaskId = subtaskIdList.get(i);
            List<UrlParamDTO> urlParamDTOList = subtaskParamLinkUrlList.get(i);

            List<SubtaskLink> res = urlParamDTOList.parallelStream().map(x -> {
                SubtaskLink subtaskLink = new SubtaskLink();
                BeanUtil.copyProperties(x, subtaskLink);
                subtaskLink.setSubtaskId(subtaskId);
                return subtaskLink;
            }).collect(Collectors.toList());

            subtaskLinkList.addAll(res);
        }

        this.saveBatch(subtaskLinkList);
    }


    @Override
    public void removeSubtaskLinks(Long subtaskId) {
        this.remove(new QueryWrapper<SubtaskLink>().eq("subtask_id", subtaskId));
    }

    @Override
    public void removeSubtaskLinks(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        QueryWrapper<SubtaskLink> queryWrapper = new QueryWrapper<>();
        subtaskIdList.forEach(x -> queryWrapper.eq("subtask_id", x).or());

        this.remove(queryWrapper);
    }

    @Override
    public void recoveryDeletedSubtaskLinks(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskLinkMapper.recoveryDeletedBySubtaskIdList(subtaskIdList);
    }

    @Override
    public void removeDeletedSubtaskLinks(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskLinkMapper.deleteDeletedBySubtaskIdList(subtaskIdList);
    }

    public UrlDTO convertUrlDTO(SubtaskLink subtaskLink) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setName(subtaskLink.getName());
        urlDTO.setUrl(subtaskLink.getUrl());
        return urlDTO;
    }
}
