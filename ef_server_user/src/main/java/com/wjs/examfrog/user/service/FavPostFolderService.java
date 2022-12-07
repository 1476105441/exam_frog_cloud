package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FavPostFolder;

import java.util.List;

public interface FavPostFolderService {
    void saveFavPostFolder(Long userId, FavPostFolder favPostFolder);

    Page listFavPostFolders(Long userId, Long favPostFolderId, Long visitedId, PageParamDTO pageParamDTO);

    void updateFavPostFolder(Long userId, FavPostFolder favPostFolder, Long favPostFolderId);

    void moveFavPostFolder(Long userId, List<Long> favPostFolderIdList, Long targetId);

    void removeFavPostFolder(Long userId, List<Long> favPostFolderIdList);
}
