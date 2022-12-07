package com.wjs.examfrog.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.*;
import com.wjs.examfrog.entity.*;
import com.wjs.examfrog.entity.Planning;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.PlanningMapper;
import com.wjs.examfrog.user.service.ApiService;
import com.wjs.examfrog.user.service.PlanningFolderService;
import com.wjs.examfrog.user.service.PlanningService;
import com.wjs.examfrog.user.service.UserService;
import com.wjs.examfrog.vo.PlanningDetailsVO;
import com.wjs.examfrog.vo.PlanningVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningServiceImpl extends ServiceImpl<PlanningMapper, Planning> implements PlanningService {

    @Resource
    private SelfIdGenerator selfIdGenerator;
    @Resource
    private ApiService apiService;
    @Resource
    private PlanningFolderService planningFolderService;
    @Resource
    private PlanningMapper planningMapper;
    @Resource
    private UserService userService;

    @Override
    public PlanningDetailsVO getPlanningDetail(Long userId, Long planningFolderId, Long planningId) {
        Planning planning = this.getById(planningId);
        if (planning == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "规划不存在");
        }

        return this.convertPlanningDetailsVO(this.getById(planningId));
    }

    @Override
    public Page listPlannings(Long userId, Long planningFolderId, PageParamDTO pageParamDTO) {
        // 如果访问的不是 根目录 那就检查存不存在
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = planningFolderService.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "访问的文件夹不存在");
            }
            if (!userId.equals(planningFolder.getUserId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "访问别人文件夹干嘛");
            }
        }

        // 查询
        Page<Planning> planningPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        QueryWrapper<Planning> queryWrapper = new QueryWrapper<Planning>()
                .eq("planning_folder_id", planningFolderId)
                .eq("author_id", userId);

        Page<Planning> page = this.page(planningPage,queryWrapper);
        return new Page<PlanningVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertPlanningVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    @GlobalTransactional
    public void savePlanning(Long userId, Long planningFolderId, PlanningParamDTO planningParamDTO) {
        // 检查
        this.checkPlanningParamDTO(planningParamDTO);

        Long planningId = selfIdGenerator.nextId(null);

        // 保存 Subtask
        if(!apiService.saveSubtasksByPlanningId(planningId, planningParamDTO.getSubtaskList()).getBody().getData())
            throw new ApiException("操作失败");

        // 保存 Planning
        Planning planning = new Planning();
        BeanUtil.copyProperties(planningParamDTO, planning);
        planning.setId(planningId);
        planning.setAuthorId(userId);
        planning.setPlanningFolderId(planningFolderId);
        planning.setFinishSubtaskCount(0L);
        planning.setTotalSubtaskCount(this.getSubtaskCount(planningParamDTO.getSubtaskList()));
        planning.setStatus(0);

        boolean res = this.save(planning);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "添加失败");
        }
    }

    @Override
    @GlobalTransactional
    public void updatePlanning(Long userId, Long planningFolderId, Long planningId, PlanningParamDTO planningParamDTO) {
        // 检查
        this.checkPlanningParamDTO(planningParamDTO);
        Planning planningDB = this.getById(planningId);
        if (planningDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "修改的规划不存在");
        }
        if (!userId.equals(planningDB.getAuthorId())) {
            throw new ApiException(ResultCode.BAD_REQUEST, "修改别人的规划干嘛");
        }

        // 修改 Subtask
        if(!apiService.updateSubtasksByPlanningId(planningId, planningParamDTO.getSubtaskList()).getBody().getData())
            throw new ApiException("操作失败");

        // 修改 Planning
        Planning planning = new Planning();
        BeanUtil.copyProperties(planningParamDTO, planning);
        planning.setId(planningId);
        planning.setFinishSubtaskCount(apiService.countFinishSubtaskCount(planningId).getBody().getData());
        planning.setTotalSubtaskCount(this.getSubtaskCount(planningParamDTO.getSubtaskList()));
        // 继承原来属性原状
        planning.setFollowUserPostId(null);

        boolean res = this.updateById(planning);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "修改失败");
        }
    }

    @Override
    @GlobalTransactional
    public void removePlannings(Long userId, Long planningFolderId, List<Long> planningIdList) {
        // 检查
        if (planningIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }
        List<Planning> planningList = this.listByIds(planningIdList);
        if (planningIdList.size() != planningList.size()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "有些规划不存在");
        }
        for (Planning planning : planningList) {
            if (!userId.equals(planning.getAuthorId())) {
                throw new ApiException(ResultCode.FORBIDDEN, "删除别人规划干嘛");
            }
        }

        // 删除 SubtaskList
        if(!apiService.removeSubtasksByPlanningIdList(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 删除
        boolean res = this.removeByIds(planningIdList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
        }
    }

    @Override
    @GlobalTransactional
    public void removePlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList) {
        if (planningFolderIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }

        // 获取全部要删除的帖子
        QueryWrapper<Planning> queryWrapper = new QueryWrapper<>();
        planningFolderIdList.forEach(x -> queryWrapper.eq("planning_folder_id", x).or());
        List<Planning> planningList = this.list(queryWrapper);

        // 检查帖子作者是否为 userId
        for (Planning planning : planningList) {
            if (!planning.getAuthorId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN, "删别人帖子干嘛");
            }
        }

        List<Long> planningIdList = planningList.parallelStream()
                .map(Planning::getId).collect(Collectors.toList());

        // 删除 SubtaskList
        if(!apiService.removeSubtasksByPlanningIdList(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 删除 UserPost
        this.removeByIds(planningIdList);
    }

    @Override
    public void movePlannings(Long userId, Long planningFolderId, List<Long> planningIdList, Long targetPlanningFolderId) {
        // 检查
        if (planningIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动的内容不能为空");
        }

        // 检验 目标文件夹 拥有者是否为 userId
        if (planningFolderId != -1) {
            PlanningFolder planningFolder = planningFolderService.getById(planningFolderId);
            if (planningFolder == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "文件夹不存在");
            }
            if (!planningFolder.getUserId().equals(userId)) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        }

        // 检验 要移动的 Planning 拥有者是否都为 userId
        List<Planning> planningListDB = this.listByIds(planningIdList);
        if (planningListDB.size() == 0 || planningIdList.size() != planningListDB.size()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "部分帖子不存在");
        }
        this.checkPlanningListBelongUserId(userId, planningListDB);

        // 构建用于 sql 的 planningList
        List<Planning> planningList = planningListDB.parallelStream()
                .map(x -> {
                    Planning planning = new Planning();
                    planning.setId(x.getId());
                    planning.setPlanningFolderId(planningFolderId);
                    return planning;
                })
                .collect(Collectors.toList());

        boolean res = this.updateBatchById(planningList);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新失败");
        }
    }

    /**
     * ==================subtask=======================
     */

    @Override
    @GlobalTransactional
    public void bingo(Long userId, Long planningId, Long subtaskId) {
        // 检测规划是否存在
        Planning planningDB = this.getById(planningId);
        if (planningDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新的规划不存在");
        }
        // 检查帖子作者是否为 userId
        if (!planningDB.getAuthorId().equals(userId)) {
            throw new ApiException(ResultCode.FORBIDDEN, "改别人规划干嘛");
        }

        // 修改 subtask
        if (!apiService.bingo(planningId, subtaskId).getBody().getData()) {
            throw new ApiException("操作失败");
        }

        // 维护 parent
        if (planningDB.getFinishSubtaskCount().equals(planningDB.getTotalSubtaskCount())) {
            Long pid = planningDB.getFollowUserPostId();
            if (pid != -1 && planningDB.getStatus() == 0) {
                if (!apiService.increaseSuccessCount(pid).getBody().getData()) {
                    throw new ApiException("更改失败");
                }

                Planning planning = new Planning();
                planning.setId(planningDB.getId());
                planning.setStatus(1);
                if (!this.updateById(planning)) {
                    throw new ApiException("操作失败");
                }
            }
        }

        // 构建用于 sql 的 Planning
        Planning planning = new Planning();
        planning.setId(planningDB.getId());
        planning.setFinishSubtaskCount(planningDB.getFinishSubtaskCount() + 1);

        // 修改
        if (!this.updateById(planning)) {
            throw new ApiException("操作失败");
        }
    }

    @Override
    @GlobalTransactional
    public void cancelBingo(Long userId, Long planningId, Long subtaskId) {
        // 检测规划是否存在
        Planning planningDB = this.getById(planningId);
        if (planningDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新的规划不存在");
        }
        // 检查帖子作者是否为 userId
        if (!planningDB.getAuthorId().equals(userId)) {
            throw new ApiException(ResultCode.FORBIDDEN, "改别人规划干嘛");
        }

        // 修改 subtask
        if(!apiService.cancelBingo(planningId, subtaskId).getBody().getData())
            throw new ApiException("操作失败");

        // 构建用于 sql 的 Planning
        Planning planning = new Planning();
        planning.setId(planningDB.getId());
        planning.setFinishSubtaskCount(planningDB.getFinishSubtaskCount() - 1);

        // 修改
        boolean res = this.updateById(planning);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新失败");
        }
    }

    /**
     * =============================回收站============================
     */
    @Override
    public Page listDeletedPlannings(Long userId, PageParamDTO pageParamDTO) {
        // 分页
        Page<Planning> planningPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 查询
        Page<Planning> page = planningMapper.selectDeletedPlannings(planningPage,userId);
        return new Page<PlanningVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertPlanningVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    @GlobalTransactional
    public void recoveryDeletedPlanningsByPlanningIdList(Long userId, List<Long> planningIdList) {
        // 检查
        if (planningIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "恢复的内容不能为空");
        }

        // 恢复 SubtaskList
        if(!apiService.recoveryDeletedSubtasks(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        int res = planningMapper.recoveryDeletedPlannings(userId, planningIdList);
        if (res <= 0) {
            throw new ApiException(ResultCode.BAD_REQUEST, "恢复失败");
        }
    }

    @Override
    @GlobalTransactional
    public void removeDeletedPlanningsByPlanningIdList(Long userId, List<Long> planningIdList) {
        // 检查
        if (planningIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "彻底删除的内容不能为空");
        }

        // 彻底删除 SubtaskList
        if(!apiService.removeDeletedSubtasks(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 彻底删除 Planning
        int res = planningMapper.deleteDeletedPlannings(userId, planningIdList);
        if (res <= 0) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
        }
    }

    @Override
    @GlobalTransactional
    public void removeAllDeletedPlanning(Long userId) {
        // 获取 planningIdList
        List<Long> planningIdList = planningMapper.selectDeletedPlanningIdListByUserId(userId);

        // 彻底删除 SubtaskList
        if(!apiService.removeDeletedSubtasks(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 彻底删除 Planning
        planningMapper.deleteAllDeletedPlannings(userId);
    }

    @Override
    @GlobalTransactional
    public void recoveryDeletedPlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            return;
        }

        // 获取 planningIdList
        List<Long> planningIdList = planningMapper.selectDeletedPlanningIdListByPlanningFolderIdList(userId, planningFolderIdList);

        // 恢复 SubtaskList
        if(!apiService.recoveryDeletedSubtasks(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 恢复 Planning
        if (planningIdList.size() > 0) {
            planningMapper.recoveryDeletedPlannings(userId, planningIdList);
        }
    }

    @Override
    @GlobalTransactional
    public void removeDeletedPlanningsByPlanningFolderIdList(Long userId, List<Long> planningFolderIdList) {
        // 检查
        if (planningFolderIdList.isEmpty()) {
            return;
        }

        // 获取 planningIdList
        List<Long> planningIdList = planningMapper.selectDeletedPlanningIdListByPlanningFolderIdList(userId, planningFolderIdList);

        // 彻底删除 SubtaskList
        if(!apiService.removeDeletedSubtasks(planningIdList).getBody().getData())
            throw new ApiException("操作失败");

        // 彻底删除 Planning
        if (planningIdList.size() > 0) {
            planningMapper.deleteDeletedPlannings(userId, planningIdList);
        }
    }

    @GlobalTransactional
    private void checkPlanningParamDTO(PlanningParamDTO planningParamDTO) {
        // 检查 categoryId 是否存在
        if (apiService.getCategoryById(planningParamDTO.getCategoryId()).getBody().getData() == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "分类不存在");
        }
    }

    public void checkPlanningListBelongUserId(Long userId, List<Planning> planningList) {
        for (Planning planning : planningList) {
            if (!userId.equals(planning.getAuthorId())) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        }
    }

    @GlobalTransactional
    public PlanningVO convertPlanningVO(Planning planning) {
        // 获取 分类
        Category category = apiService.getCategoryById(planning.getCategoryId()).getBody().getData();

        // 构建 PlanningVO
        PlanningVO planningVO = new PlanningVO();
        BeanUtil.copyProperties(planning, planningVO);
        planningVO.setCategoryName(category.getName());

        return planningVO;
    }

    public List<PlanningVO> convertPlanningVOs(List<Planning> planningList) {
        return planningList.parallelStream()
                .map(this::convertPlanningVO)
                .collect(Collectors.toList());
    }


    public PlanningDetailsVO convertPlanningDetailsVO(Planning planning) {
        // 获取 分类
        Category category = apiService.getCategoryById(planning.getCategoryId()).getBody().getData();
        // 获取 作者
        User user = userService.getById(planning.getAuthorId());

        // 获取 SubtaskDTOList
        List<SubtaskDTO> subtaskDTOList = apiService.listSubtasksByPlanningId(planning.getId()).getBody().getData();

        // 获取 LinkUrlList
        List<UrlDTO> linkUrlList = new ArrayList<>();
        for (SubtaskDTO subtaskDTO : subtaskDTOList) {
            linkUrlList.addAll(subtaskDTO.getLinkUrlList());
        }

        // 构建 UserPostDetailsVO
        PlanningDetailsVO planningDetailsVO = new PlanningDetailsVO();
        BeanUtil.copyProperties(planning, planningDetailsVO);
        planningDetailsVO.setCategoryName(category.getName());
        planningDetailsVO.setSubtaskList(subtaskDTOList);
        planningDetailsVO.setAuthorNickName(user.getNickName());
        planningDetailsVO.setLinkUrlList(linkUrlList);

        return planningDetailsVO;
    }

    private Long getSubtaskCount(List<SubtaskParamDTO> subtaskParamDTOList) {
        // 统计 非根节点
        // 统计入度
        int[] cnt = new int[10010];
        subtaskParamDTOList.forEach(x -> {
            if (x.getPidx() >= 0) {
                cnt[x.getPidx()]++;
            }
        });

        return subtaskParamDTOList.size() - subtaskParamDTOList.parallelStream()
                .filter(x -> cnt[x.getIdx()] == 0).count();
    }

}
