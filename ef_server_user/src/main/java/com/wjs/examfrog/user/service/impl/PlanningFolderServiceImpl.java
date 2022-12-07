package com.wjs.examfrog.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PlanningParamDTO;
import com.wjs.examfrog.entity.PlanningFolder;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.PlanningFolderMapper;
import com.wjs.examfrog.user.service.PlanningFolderService;
import com.wjs.examfrog.user.service.PlanningService;
import com.wjs.examfrog.vo.PlanningDetailsVO;
import com.wjs.examfrog.vo.PlanningFolderVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PlanningFolderServiceImpl extends ServiceImpl<PlanningFolderMapper, PlanningFolder> implements PlanningFolderService {

    @Resource
    private SelfIdGenerator selfIdGenerator;
    @Resource
    private PlanningFolderMapper planningFolderMapper;
    @Resource
    private PlanningService planningService;

    @Override
    public Page listPlanningFolders(Long userId, Long planningFolderId, PageParamDTO pageParamDTO) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!planningFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        // 分页
        Page<PlanningFolder> folderPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 构造条件
        QueryWrapper<PlanningFolder> queryWrapper = new QueryWrapper<PlanningFolder>()
                .eq("user_id", userId)
                .eq("pid", planningFolderId)
                .orderByDesc("gmt_create");

        // 查询
        Page<PlanningFolder> page = this.page(folderPage,queryWrapper);

        return new Page<PlanningFolderVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertPlanningFolderVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void savePlanningFolder(Long userId, String planningFolderName, Long pid) {
        // 如果不是在 根目录 创建, 检查下存不存在
        if (pid != -1) {
            PlanningFolder planningFolderDB = this.getById(pid);
            if (planningFolderDB == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "父文件夹不存在");
            }
        }

        PlanningFolder planningFolder = new PlanningFolder();
        planningFolder.setId(selfIdGenerator.nextId(null));
        planningFolder.setUserId(userId);
        planningFolder.setName(planningFolderName);
        planningFolder.setPid(pid);

        boolean res = this.save(planningFolder);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "添加失败");
        }
    }

    @Override
    public void updatePlanningFolder(Long userId, Long planningFolderId, String planningFolderName) {
        // 检查
        if (planningFolderId == -1) {
            throw new ApiException(ResultCode.BAD_REQUEST, "根目录不允许修改");
        }
        PlanningFolder planningFolderDB = this.getById(planningFolderId);
        if (planningFolderDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "文件夹不存在");
        }

        // 构建用于 sql 的 PlanningFolder
        PlanningFolder planningFolder = new PlanningFolder();
        planningFolder.setId(planningFolderDB.getId());
        planningFolder.setName(planningFolderName);

        boolean res = this.updateById(planningFolder);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "修改失败");
        }
    }

    @Override
    public void removePlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }
        for (Long planningFolderId : planningFolderIdList) {
            if (planningFolderId == -1) {
                throw new ApiException(ResultCode.FORBIDDEN, "根目录不允许被删除");
            }
        }
        List<PlanningFolder> planningFolderListDB = this.listByIds(planningFolderIdList);
        if (planningFolderIdList.size() != planningFolderListDB.size()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "部分文件夹不存在");
        }
        for (PlanningFolder planningFolder : planningFolderListDB) {
            if (!planningFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "删别人文件夹干嘛");
            }
        }

        List<Long> wholePlanningFolderIdList = this.getWholePlanningFolderIdList(userId, planningFolderIdList);
        planningService.removePlanningsByPlanningFolderIdList(userId, new ArrayList<>(wholePlanningFolderIdList));

        boolean res = removeByIds(wholePlanningFolderIdList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
        }
    }

    @Override
    public void movePlanningFolders(Long userId, List<Long> planningFolderIdList, Long targetPlanningFolderId) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动的内容不能为空");
        }
        for (Long planningFolderId : planningFolderIdList) {
            if (planningFolderId == -1) {
                throw new ApiException(ResultCode.FORBIDDEN, "根目录不允许被移动");
            }
        }
        List<PlanningFolder> planningFolderListDB = this.listByIds(planningFolderIdList);
        if (planningFolderIdList.size() != planningFolderListDB.size()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "部分文件夹不存在");
        }
        for (PlanningFolder planningFolder : planningFolderListDB) {
            if (!planningFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "移动别人文件夹干嘛");
            }
        }
        List<PlanningFolder> wholePlanningFolderList = this.getWholePlanningFolderList(userId, planningFolderIdList);
        for (PlanningFolder planningFolder : wholePlanningFolderList) {
            if (targetPlanningFolderId.equals(planningFolder.getPid())) {
                throw new ApiException(ResultCode.BAD_REQUEST, "不能移动到子文件夹内");
            }
        }

        // 构建用于 sql 的 planningFolderList
        List<PlanningFolder> planningFolderList = planningFolderListDB.parallelStream()
                .map(x -> {
                    PlanningFolder planningFolder = new PlanningFolder();
                    planningFolder.setId(x.getId());
                    planningFolder.setPid(targetPlanningFolderId);
                    return planningFolder;
                })
                .collect(Collectors.toList());

        boolean res = updateBatchById(planningFolderList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
        }
    }

    /**
     * ========================Planning==================================
     */
    @Override
    public PlanningDetailsVO getPlanningDetail(Long userId, Long planningFolderId, Long planningId) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!planningFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        return planningService.getPlanningDetail(userId, planningFolderId, planningId);
    }

    @Override
    public Page listPlannings(Long userId, Long planningFolderId, PageParamDTO pageParamDTO) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        return planningService.listPlannings(userId, planningFolderId, pageParamDTO);
    }

    @Override
    public void savePlanning(Long userId, Long planningFolderId, PlanningParamDTO planningParamDTO) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        planningService.savePlanning(userId, planningFolderId, planningParamDTO);
    }

    @Override
    public void updatePlanning(Long userId, Long planningFolderId, Long userPostId, PlanningParamDTO planningParamDTO) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        planningService.updatePlanning(userId, planningFolderId, userPostId, planningParamDTO);
    }

    @Override
    public void removePlannings(Long userId, Long planningFolderId, List<Long> planningList) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        planningService.removePlannings(userId, planningFolderId, planningList);
    }

    @Override
    public void movePlannings(Long userId, Long planningFolderId, List<Long> userPostIdList, Long targetPlanningFolderId) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = this.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        planningService.movePlannings(userId, planningFolderId, userPostIdList, targetPlanningFolderId);
    }

    /**
     * =============================回收站============================
     */

    @Override
    public Page listDeletedPlanningFolders(Long userId, PageParamDTO pageParamDTO) {
        // 设置分页
        Page<PlanningFolder> folderPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 查询
        Page<PlanningFolder> page = planningFolderMapper.selectDeletedPlanningFolders(folderPage,userId);
        return new Page<PlanningFolderVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertPlanningFolderVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void recoveryDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "恢复的内容不能为空");
        }

        List<Long> wholePlanningFolderIdList = this.getWholePlanningFolderIdList(userId, planningFolderIdList);
        // 彻底删除 Planning
        planningService.recoveryDeletedPlanningsByPlanningFolderIdList(userId, wholePlanningFolderIdList);

        int res = planningFolderMapper.recoveryDeletedPlanningFolders(userId, planningFolderIdList);
        if (res <= 0) {
            throw new ApiException(ResultCode.BAD_REQUEST, "恢复失败");
        }
    }

    @Override
    public void removeDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }

        List<Long> wholePlanningFolderIdList = this.getWholePlanningFolderIdList(userId, planningFolderIdList);
        // 彻底删除 Planning
        planningService.removeDeletedPlanningsByPlanningFolderIdList(userId, wholePlanningFolderIdList);

        // 彻底删除 PlanningFolder
        int res = planningFolderMapper.deleteDeletedPlanningFolders(userId, planningFolderIdList);
        if (res <= 0) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
        }
    }

    @Override
    public void removeAllDeletedPlanningFolders(Long userId) {
        // 彻底删除全部 PlanningFolder
        planningFolderMapper.deleteAllDeletedPlanningFolders(userId);
    }

    public PlanningFolderVO convertPlanningFolderVO(PlanningFolder planningFolder) {
        PlanningFolderVO planningFolderVO = new PlanningFolderVO();
        BeanUtil.copyProperties(planningFolder, planningFolderVO);
        planningFolderVO.setPlanningFolderName(planningFolder.getName());
        return planningFolderVO;
    }

    public List<PlanningFolderVO> convertPlanningFolderVOs(List<PlanningFolder> planningFolderList) {
        return planningFolderList.parallelStream()
                .map(this::convertPlanningFolderVO)
                .collect(Collectors.toList());
    }

    public List<Long> getWholePlanningFolderIdList(Long userId, List<Long> planningFolderIdList) {
        return getWholePlanningFolderList(userId, planningFolderIdList).parallelStream()
                .map(PlanningFolder::getId)
                .collect(Collectors.toList());
    }

    public List<PlanningFolder> getWholePlanningFolderList(Long userId, List<Long> planningFolderIdList) {
        List<PlanningFolder> planningFolderList = this.list(new QueryWrapper<PlanningFolder>()
                .eq("user_id", userId));

        // 层次遍历 获取所有相关的 planningFolderIdSet
        Set<Long> planningFolderIdSet = new HashSet<>();
        Queue<Long> que = new LinkedList<>(planningFolderIdList);
        while (!que.isEmpty()) {
            Long cur = que.poll();
            if (planningFolderIdSet.contains(cur)) {
                continue;
            }
            planningFolderIdSet.add(cur);
            for (PlanningFolder planningFolder : planningFolderList) {
                if (planningFolder.getPid().equals(cur)) {
                    que.add(planningFolder.getId());
                }
            }
        }

        return planningFolderList.parallelStream()
                .filter(x -> planningFolderIdSet.contains(x.getId()))
                .collect(Collectors.toList());
    }


}
