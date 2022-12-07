package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FavPostFolder;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.FavPostFolderMapper;
import com.wjs.examfrog.user.service.FavPostFolderService;
import com.wjs.examfrog.vo.FavPostFolderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FavPostFolderServiceImpl extends ServiceImpl<FavPostFolderMapper, FavPostFolder> implements FavPostFolderService {

    @Resource
    private SelfIdGenerator selfIdGenerator;

    @Override
    public void saveFavPostFolder(Long userId, FavPostFolder favPostFolder) {
        // 如果不是在根目录，检查存不存在
        Long pid = favPostFolder.getPid();
        FavPostFolder favPostFolderDB = this.getById(pid);
        if (pid != -1) {
            if (favPostFolderDB == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "父文件夹不存在");
            }
        }
        QueryWrapper<FavPostFolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", favPostFolder.getName()).eq("user_id", userId);
        FavPostFolder favPostFolderDB2 = this.getOne(queryWrapper);
        if(favPostFolderDB2 != null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "文件夹名称不可重复");
        }

        favPostFolder.setId(selfIdGenerator.nextId(null));
        favPostFolder.setUserId(userId);

        boolean res = this.save(favPostFolder);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "添加失败");
        }
    }

    @Override
    public Page listFavPostFolders(Long userId, Long pid, Long visitedId, PageParamDTO pageParamDTO) {
        // 如果不是在根目录 检查存不存在
        if (pid != -1) {
            FavPostFolder favPostFolder = this.getById(pid);
            if (favPostFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!favPostFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人的文件夹干嘛");
            }
        }

        // 分页
        Page<FavPostFolder> folderPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 构造条件
        QueryWrapper<FavPostFolder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", visitedId)
                    .eq("pid", pid);
        // 他人访问无法访问私有
        // 不能用等于号，即使数值相等也会判定为不相等
        if (!userId.equals(visitedId)) {
            queryWrapper.eq("is_open", true);
        }

        queryWrapper.orderByDesc("gmt_modified");

        // 查询
        Page<FavPostFolder> page = this.page(folderPage,queryWrapper);

        return new Page<FavPostFolderVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertFavPostFolderVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    /**
     * 修改文件夹名
     * @param userId
     * @param favPostFolderId
     * @param favPostFolder
     */
    @Override
    public void updateFavPostFolder(Long userId, FavPostFolder favPostFolder, Long favPostFolderId) {
        // 检查
        FavPostFolder favPostFolderDB = this.getById(favPostFolderId);
        if (favPostFolderDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "文件夹不存在");
        }
        if (!favPostFolderDB.getUserId().equals(userId)) {
            throw new ApiException(ResultCode.BAD_REQUEST, "无权修改别人文件夹");
        }
        favPostFolder.setId(favPostFolderId);
        boolean res = this.updateById(favPostFolder);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "修改失败");
        }
    }

    @Override
    public void moveFavPostFolder(Long userId, List<Long> favPostFolderIdList, Long targetId) {
        // 检查
        if (favPostFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动的内容不能为空");
        }
        for (Long favPostFolderId : favPostFolderIdList) {
            if (favPostFolderId == -1) {
                throw new ApiException(ResultCode.FORBIDDEN, "根目录不允许被移动");
            }
        }
        List<FavPostFolder> favPostFolderInfoList = this.listByIds(favPostFolderIdList);
        for (FavPostFolder favPostFolder : favPostFolderInfoList) {
            if (targetId == favPostFolder.getId()) {
                throw new ApiException(ResultCode.BAD_REQUEST, "不允许移动到子文件夹内");
            }
        }
        // 移动
        for (FavPostFolder favPostFolder : favPostFolderInfoList) {
            favPostFolder.setPid(targetId);
        }
        boolean res = this.updateBatchById(favPostFolderInfoList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
        }
    }

    @Override
    public void removeFavPostFolder(Long userId, List<Long> favPostFolderIdList) {
        // 检查
        if (favPostFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }
        for (Long favPostFolderId : favPostFolderIdList) {
            if (favPostFolderId == -1) {
                throw new ApiException(ResultCode.BAD_REQUEST, "根目录不允许被删除");
            }
        }
        List<FavPostFolder> favPostFolderListDB = this.listByIds(favPostFolderIdList);
        if (favPostFolderListDB.size() != favPostFolderIdList.size()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "部分文件夹不存在");
        }

        // 顺带把文件夹内的内容删除？

        boolean res = this.removeByIds(favPostFolderIdList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "部分文件夹不存在");
        }
    }

    private List<FavPostFolderVO> convertFavPostFolderVOs(List<FavPostFolder> favPostFolders) {
        return favPostFolders.parallelStream()
                .map(this::convertFavPostFolderVO)
                .collect(Collectors.toList());
    }

    private FavPostFolderVO convertFavPostFolderVO(FavPostFolder favPostFolder) {
        FavPostFolderVO favPostFolderVO = new FavPostFolderVO();
        BeanUtils.copyProperties(favPostFolder, favPostFolderVO);
        favPostFolderVO.setFavPostFolderName(favPostFolder.getName());
        return favPostFolderVO;
    }
}
