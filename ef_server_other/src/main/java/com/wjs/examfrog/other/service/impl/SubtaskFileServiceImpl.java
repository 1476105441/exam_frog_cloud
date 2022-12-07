package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.SubtaskFile;
import com.wjs.examfrog.other.mapper.SubtaskFileMapper;
import com.wjs.examfrog.other.service.SubtaskFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SubtaskFileServiceImpl extends ServiceImpl<SubtaskFileMapper, SubtaskFile> implements SubtaskFileService {

    @Resource
    private SubtaskFileMapper subtaskFileMapper;

    @Override
    public Map<Long, List<UrlDTO>> getMapBySubtaskIdList(List<Long> subtaskIdList) {
        List<SubtaskFile> subtaskFileList = this.listSubtaskFilesBySubtaskIdList(subtaskIdList);
        Map<Long, List<UrlDTO>> res = new HashMap<>();
        for (SubtaskFile subtaskFile : subtaskFileList) {
            List<UrlDTO> urlDTOList = res.getOrDefault(subtaskFile.getSubtaskId(), new ArrayList<>());
            urlDTOList.add(this.convertUrlDTO(subtaskFile));
        }

        return res;
    }

    @Override
    public List<SubtaskFile> listSubtaskFilesBySubtaskIdList(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) return new ArrayList<>();

        QueryWrapper<SubtaskFile> queryWrapper = new QueryWrapper<>();
        for (Long subtaskId : subtaskIdList) {
            queryWrapper.eq("subtask_id", subtaskId).or();
        }

        return this.list(queryWrapper);
    }

    @Override
    public void saveSubtaskFiles(Long subtaskId, List<UrlParamDTO> urlParamDTOList) {
        List<SubtaskFile> subtaskFileList = urlParamDTOList.parallelStream()
                .map(x -> {
                    SubtaskFile subtaskFile = new SubtaskFile();
                    BeanUtil.copyProperties(x, subtaskFile);
                    subtaskFile.setSubtaskId(subtaskId);

                    return subtaskFile;
                }).collect(Collectors.toList());

        this.saveBatch(subtaskFileList);
    }

    @Override
    public void saveSubtaskFiles(List<Long> subtaskIdList, List<List<UrlParamDTO>> subtaskParamFileUrlList) {
        List<SubtaskFile> subtaskFileList = new ArrayList<>();

        for (int i = 0; i < subtaskIdList.size(); i++) {
            Long subtaskId = subtaskIdList.get(i);
            List<UrlParamDTO> urlParamDTOList = subtaskParamFileUrlList.get(i);

            List<SubtaskFile> res = urlParamDTOList.parallelStream()
                    .map(x -> {
                        SubtaskFile subtaskFile = new SubtaskFile();
                        BeanUtil.copyProperties(x, subtaskFile);
                        subtaskFile.setSubtaskId(subtaskId);

                        return subtaskFile;
                    }).collect(Collectors.toList());

            subtaskFileList.addAll(res);
        }

        this.saveBatch(subtaskFileList);
    }

    @Override
    public void removeSubtaskFiles(Long subtaskId) {
        this.remove(new QueryWrapper<SubtaskFile>().eq("subtask_id", subtaskId));
    }

    @Override
    public void removeSubtaskFiles(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        QueryWrapper<SubtaskFile> queryWrapper = new QueryWrapper<>();
        subtaskIdList.forEach(x -> queryWrapper.eq("subtask_id", x).or());

        this.remove(queryWrapper);
    }

    @Override
    public void recoveryDeletedSubtaskFiles(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskFileMapper.recoveryDeletedBySubtaskIdList(subtaskIdList);
    }

    @Override
    public void removeDeletedSubtaskFiles(List<Long> subtaskIdList) {
        if (subtaskIdList.isEmpty()) {
            return;
        }

        subtaskFileMapper.deleteDeletedBySubtaskIdList(subtaskIdList);
    }

    public UrlDTO convertUrlDTO(SubtaskFile subtaskFile) {
        UrlDTO urlDTO = new UrlDTO();
        urlDTO.setName(subtaskFile.getName());
        urlDTO.setUrl(subtaskFile.getUrl());
        return urlDTO;
    }

}
