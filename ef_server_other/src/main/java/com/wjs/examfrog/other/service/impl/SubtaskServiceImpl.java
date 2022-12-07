package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.SubtaskParamDTO;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.dto.UrlParamDTO;
import com.wjs.examfrog.entity.Subtask;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.mapper.SubtaskMapper;
import com.wjs.examfrog.other.service.SubtaskFileService;
import com.wjs.examfrog.other.service.SubtaskImageService;
import com.wjs.examfrog.other.service.SubtaskLinkService;
import com.wjs.examfrog.other.service.SubtaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubtaskServiceImpl extends ServiceImpl<SubtaskMapper, Subtask> implements SubtaskService {

    @Resource
    private SelfIdGenerator selfIdGenerator;
    @Resource
    private SubtaskFileService subtaskFileService;
    @Resource
    private SubtaskImageService subtaskImageService;
    @Resource
    private SubtaskLinkService subtaskLinkService;
    @Resource
    private SubtaskMapper subtaskMapper;

    @Override
    public List<SubtaskDTO> listSubtasksByUserPostId(Long userPostId) {
        List<Subtask> subtaskList = this.list(new QueryWrapper<Subtask>().eq("user_post_id", userPostId));
        return this.convertSubtaskDTOs(subtaskList);
    }

    @Override
    public List<SubtaskDTO> listSubtasksByPlanningId(Long planningId) {
        List<Subtask> subtaskList = this.list(new QueryWrapper<Subtask>().eq("planning_id", planningId));
        return this.convertSubtaskDTOs(subtaskList);
    }

    @Override
    public Boolean saveSubtasksByPlanningId(Long planningId, List<SubtaskParamDTO> subtaskParamDTOList) {
        // 检查 subtaskList 结构是否合法
        this.checkSubtaskList(subtaskParamDTOList);

        // 先排序
        subtaskParamDTOList.sort(Comparator.comparing(SubtaskParamDTO::getIdx));

        // 构成 subtaskList
        List<Subtask> subtaskList = subtaskParamDTOList.parallelStream()
                .map(x -> {
                    Long subtaskId = selfIdGenerator.nextId(null);

                    Subtask subtask = new Subtask();
                    BeanUtil.copyProperties(x, subtask);
                    subtask.setId(subtaskId);
                    subtask.setPlanningId(planningId);
                    subtask.setIsFinish(false);

                    return subtask;
                }).collect(Collectors.toList());

        // 赋值 结构信息
        this.assignSubtaskStructure(subtaskList, subtaskParamDTOList);

        // 插入 fileUrlList, imageUrlList, linkUrlList
        List<Long> subtaskIdList = subtaskList.parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamFileUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getFileUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamImageUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getImageUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamLinkUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getLinkUrlList)
                .collect(Collectors.toList());

        // 插入
        subtaskFileService.saveSubtaskFiles(subtaskIdList, subtaskParamFileUrlList);
        subtaskImageService.saveSubtaskImages(subtaskIdList, subtaskParamImageUrlList);
        subtaskLinkService.saveSubtaskLinks(subtaskIdList, subtaskParamLinkUrlList);

        // 插入 subtaskList
        return this.saveBatch(subtaskList);
    }

    @Override
    public void saveSubtasksByUserPostId(Long userPostId, List<SubtaskParamDTO> subtaskParamDTOList) {
        // 检查 subtaskList 结构是否合法
        this.checkSubtaskList(subtaskParamDTOList);

        // 先排序
        subtaskParamDTOList.sort(Comparator.comparing(SubtaskParamDTO::getIdx));

        // 构成 subtaskList
        List<Subtask> subtaskList = subtaskParamDTOList.parallelStream()
                .map(x -> {
                    Long subtaskId = selfIdGenerator.nextId(null);

                    Subtask subtask = new Subtask();
                    BeanUtil.copyProperties(x, subtask);
                    subtask.setId(subtaskId);
                    subtask.setUserPostId(userPostId);
                    subtask.setIsFinish(false);

                    return subtask;
                }).collect(Collectors.toList());

        // 赋值 结构信息
        this.assignSubtaskStructure(subtaskList, subtaskParamDTOList);

        // 插入 fileUrlList, imageUrlList, linkUrlList
        List<Long> subtaskIdList = subtaskList.parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamFileUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getFileUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamImageUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getImageUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamLinkUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getLinkUrlList)
                .collect(Collectors.toList());

        // 插入
        subtaskFileService.saveSubtaskFiles(subtaskIdList, subtaskParamFileUrlList);
        subtaskImageService.saveSubtaskImages(subtaskIdList, subtaskParamImageUrlList);
        subtaskLinkService.saveSubtaskLinks(subtaskIdList, subtaskParamLinkUrlList);

        // 插入 subtaskList
        this.saveBatch(subtaskList);
    }

    @Override
    public void updateSubtasksByUserPostId(Long userPostId, List<SubtaskParamDTO> subtaskParamDTOList) {
        // 检查 subtaskList 结构是否合法
        this.checkSubtaskList(subtaskParamDTOList);

        // 用 userPostId 做内容的哈希映射, 如果存在, 就继承 isFinish字段
        // ps: Subtask 的 hashCode 已重写, 用作计算 hashCode 的都是内容字段
        List<Subtask> findSubtaskList = this.list(new QueryWrapper<Subtask>().eq("user_post_id", userPostId));
        HashMap<Integer, Boolean> hash = new HashMap<>();
        for (Subtask subtask : findSubtaskList) {
            hash.put(subtask.hashCode(), subtask.getIsFinish());
        }

        // 先排序
        subtaskParamDTOList.sort(Comparator.comparing(SubtaskParamDTO::getIdx));

        // 构成 subtaskList
        List<Subtask> subtaskList = subtaskParamDTOList.parallelStream()
                .map(x -> {
                    Long subtaskId = selfIdGenerator.nextId(null);

                    Subtask subtask = new Subtask();
                    BeanUtil.copyProperties(x, subtask);
                    subtask.setId(subtaskId);
                    subtask.setUserPostId(userPostId);
                    subtask.setIsFinish(hash.get(subtask.hashCode()) != null && hash.get(subtask.hashCode()));

                    return subtask;
                }).collect(Collectors.toList());

        // 赋值 结构信息
        this.assignSubtaskStructure(subtaskList, subtaskParamDTOList);

        // 插入 fileUrlList, imageUrlList, linkUrlList
        List<Long> subtaskIdList = subtaskList.parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamFileUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getFileUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamImageUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getImageUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamLinkUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getLinkUrlList)
                .collect(Collectors.toList());

        // 插入
        subtaskFileService.saveSubtaskFiles(subtaskIdList, subtaskParamFileUrlList);
        subtaskImageService.saveSubtaskImages(subtaskIdList, subtaskParamImageUrlList);
        subtaskLinkService.saveSubtaskLinks(subtaskIdList, subtaskParamLinkUrlList);

        // 插入 subtaskList
        this.removeSubtasksByUserPostId(userPostId);
        this.saveBatch(subtaskList);
    }

    @Override
    public Boolean updateSubtasksByPlanningId(Long planningId, List<SubtaskParamDTO> subtaskParamDTOList) {
        // 检查 subtaskList 结构是否合法
        this.checkSubtaskList(subtaskParamDTOList);

        // 用 userPostId 做内容的哈希映射, 如果存在, 就继承 isFinish字段
        // ps: Subtask 的 hashCode 已重写, 用作计算 hashCode 的都是内容字段
        List<Subtask> findSubtaskList = this.list(new QueryWrapper<Subtask>().eq("planning_id", planningId));
        HashMap<Integer, Boolean> hash = new HashMap<>();
        for (Subtask subtask : findSubtaskList) {
            hash.put(subtask.hashCode(), subtask.getIsFinish());
        }

        // 先排序
        subtaskParamDTOList.sort(Comparator.comparing(SubtaskParamDTO::getIdx));

        // 构成 subtaskList
        List<Subtask> subtaskList = subtaskParamDTOList.parallelStream()
                .map(x -> {
                    Long subtaskId = selfIdGenerator.nextId(null);

                    Subtask subtask = new Subtask();
                    BeanUtil.copyProperties(x, subtask);
                    subtask.setId(subtaskId);
                    subtask.setPlanningId(planningId);
                    subtask.setIsFinish(hash.get(subtask.hashCode()) != null && hash.get(subtask.hashCode()));

                    return subtask;
                }).collect(Collectors.toList());

        // 赋值 结构信息
        this.assignSubtaskStructure(subtaskList, subtaskParamDTOList);

        // 插入 fileUrlList, imageUrlList, linkUrlList
        List<Long> subtaskIdList = subtaskList.parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamFileUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getFileUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamImageUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getImageUrlList)
                .collect(Collectors.toList());
        List<List<UrlParamDTO>> subtaskParamLinkUrlList = subtaskParamDTOList.parallelStream()
                .map(SubtaskParamDTO::getLinkUrlList)
                .collect(Collectors.toList());

        // 插入
        subtaskFileService.saveSubtaskFiles(subtaskIdList, subtaskParamFileUrlList);
        subtaskImageService.saveSubtaskImages(subtaskIdList, subtaskParamImageUrlList);
        subtaskLinkService.saveSubtaskLinks(subtaskIdList, subtaskParamLinkUrlList);

        // 插入 subtaskList
        this.removeSubtasksByPlanningId(planningId);
        return this.saveBatch(subtaskList);
    }

    @Override
    public void removeSubtasksByPlanningId(Long planningId) {
        // 获取 subtaskIdList
        List<Long> subtaskIdList = this.list(new QueryWrapper<Subtask>().eq("planning_id", planningId)).parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());

        // 删除 SubtaskFileList
        subtaskFileService.removeSubtaskFiles(subtaskIdList);

        // 删除 SubtaskImageList
        subtaskImageService.removeSubtaskImages(subtaskIdList);

        // 删除 SubtaskLinkList
        subtaskLinkService.removeSubtaskLinks(subtaskIdList);

        // 删除 Subtask
        this.removeByIds(subtaskIdList);
    }

    @Override
    public void removeSubtasksByUserPostId(Long userPostId) {
        // 获取 subtaskIdList
        List<Long> subtaskIdList = this.list(new QueryWrapper<Subtask>().eq("user_post_id", userPostId)).parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());

        // 删除 SubtaskFileList
        subtaskFileService.removeSubtaskFiles(subtaskIdList);

        // 删除 SubtaskImageList
        subtaskImageService.removeSubtaskImages(subtaskIdList);

        // 删除 SubtaskLinkList
        subtaskLinkService.removeSubtaskLinks(subtaskIdList);

        // 删除 Subtask
        this.removeByIds(subtaskIdList);
    }

    @Override
    public void removeSubtasksByUserPostId(List<Long> userPostIdList) {
        if (userPostIdList.isEmpty()) {
            return;
        }

        // 获取相关的 subtaskIdList
        QueryWrapper<Subtask> queryWrapper = new QueryWrapper<>();
        userPostIdList.forEach(x -> queryWrapper.eq("user_post_id", x).or());
        List<Long> subtaskIdList = this.list(queryWrapper).parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());

        // 删除 SubtaskFileList
        subtaskFileService.removeSubtaskFiles(subtaskIdList);

        // 删除 SubtaskImageList
        subtaskImageService.removeSubtaskImages(subtaskIdList);

        // 删除 SubtaskLinkList
        subtaskLinkService.removeSubtaskLinks(subtaskIdList);

        // 删除 Subtask
        this.removeByIds(subtaskIdList);
    }

    @Override
    public Boolean removeSubtasksByPlanningIdList(List<Long> planningIdList) {
        if (planningIdList.isEmpty()) {
            return false;
        }

        // 获取相关的 subtaskIdList
        QueryWrapper<Subtask> queryWrapper = new QueryWrapper<>();
        planningIdList.forEach(x -> queryWrapper.eq("planning_id", x).or());
        List<Long> subtaskIdList = this.list(queryWrapper).parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());

        // 删除 SubtaskFileList
        subtaskFileService.removeSubtaskFiles(subtaskIdList);

        // 删除 SubtaskImageList
        subtaskImageService.removeSubtaskImages(subtaskIdList);

        // 删除 SubtaskLinkList
        subtaskLinkService.removeSubtaskLinks(subtaskIdList);

        // 删除 Subtask
        return this.removeByIds(subtaskIdList);
    }

    @Override
    public Long countFinishSubtaskCount(Long planningId) {
        long cnt = 0L;
        List<Subtask> subtaskList = this.list(new QueryWrapper<Subtask>().eq("planning_id", planningId));
        for (Subtask subtask : subtaskList) {
            cnt += subtask.getIsFinish() ? 1 : 0;
        }

        return cnt;
    }

    @Override
    public Boolean bingo(Long userPostId, Long subtaskId) {
        // 检测目标是否存在
        Subtask subtaskDB = this.getById(subtaskId);
        if (subtaskDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新的目标不存在");
        }
        // 检查目标是否为 属于 userPostId
        if (!subtaskDB.getUserPostId().equals(userPostId)) {
            throw new ApiException(ResultCode.BAD_REQUEST, "参数不对劲");
        }
        // 检测是否已经完成
        if (subtaskDB.getIsFinish()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "已经完成了");
        }

        // 修改 subtask
        Subtask subtask = new Subtask();
        subtask.setPlanningId(subtaskId);
        subtask.setIsFinish(true);

        return this.updateById(subtask);
    }

    @Override
    public Boolean cancelBingo(Long userPostId, Long subtaskId) {
        // 检测目标是否存在
        Subtask subtaskDB = this.getById(subtaskId);
        if (subtaskDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新的目标不存在");
        }
        // 检查目标是否为 属于 userPostId
        if (!subtaskDB.getUserPostId().equals(userPostId)) {
            throw new ApiException(ResultCode.BAD_REQUEST, "参数不对劲");
        }
        // 检测是否未完成
        if (!subtaskDB.getIsFinish()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "已经取消完成了");
        }

        // 修改 subtask
        Subtask subtask = new Subtask();
        subtask.setPlanningId(subtaskId);
        subtask.setIsFinish(false);

        return this.updateById(subtask);
    }

    @Override
    public Boolean recoveryDeletedSubtasks(List<Long> planningIdList) {
        if (planningIdList.isEmpty()) {
            return false;
        }

        // 获取 subtaskIdList
        List<Long> subtaskIdList = subtaskMapper.selectDeletedSubtaskIdListByPlanningIdList(planningIdList);

        // 恢复 SubtaskFileList
        subtaskFileService.recoveryDeletedSubtaskFiles(subtaskIdList);
        // 恢复 SubtaskImageList
        subtaskImageService.recoveryDeletedSubtaskImages(subtaskIdList);
        // 恢复 SubtaskLinkList
        subtaskLinkService.recoveryDeletedSubtaskLinks(subtaskIdList);

        // 恢复 Subtask
        return subtaskMapper.recoveryDeletedSubtasksByPlanningIdList(planningIdList);
    }

    @Override
    public Boolean removeDeletedSubtasks(List<Long> planningIdList) {
        if (planningIdList.isEmpty()) {
            return false;
        }

        // 获取 subtaskIdList
        List<Long> subtaskIdList = subtaskMapper.selectDeletedSubtaskIdListByPlanningIdList(planningIdList);

        // 彻底删除 SubtaskFileList
        subtaskFileService.removeDeletedSubtaskFiles(subtaskIdList);
        // 彻底删除 SubtaskImageList
        subtaskImageService.removeDeletedSubtaskImages(subtaskIdList);
        // 彻底删除 SubtaskLinkList
        subtaskLinkService.removeDeletedSubtaskLinks(subtaskIdList);

        // 彻底删除 Subtask
        return subtaskMapper.deleteDeletedSubtasksByPlanningIdList(planningIdList);
    }

    /**
     * =======================非业务函数=========================
     */
    /**
     * 联表查询
     * 1. 通过 userPostId 查询 UserPostSubtaskRelation(关联表)
     * 2. 遍历关联表 构建 SubtaskDTO
     */
    public List<SubtaskDTO> convertSubtaskDTOs(List<Subtask> subtaskList) {
        List<Long> subtaskIdList = subtaskList.parallelStream()
                .map(Subtask::getId)
                .collect(Collectors.toList());

        Map<Long, List<UrlDTO>> subtaskFileMap = subtaskFileService.getMapBySubtaskIdList(subtaskIdList);
        Map<Long, List<UrlDTO>> subtaskImageMap = subtaskImageService.getMapBySubtaskIdList(subtaskIdList);
        Map<Long, List<UrlDTO>> subtaskLinkMap = subtaskLinkService.getMapBySubtaskIdList(subtaskIdList);

        return subtaskList.parallelStream()
                .map(x -> {
                    // 获取 文件数组
                    List<UrlDTO> fileUrlList = subtaskFileMap.getOrDefault(x.getId(), new ArrayList<>());
                    // 获取 图片数组
                    List<UrlDTO> imageUrlList = subtaskImageMap.getOrDefault(x.getId(), new ArrayList<>());
                    // 获取 链接数组
                    List<UrlDTO> linkUrlList = subtaskLinkMap.getOrDefault(x.getId(), new ArrayList<>());

                    // 构建 SubtaskDTO
                    SubtaskDTO subtaskDTO = new SubtaskDTO();
                    BeanUtil.copyProperties(x, subtaskDTO);
                    subtaskDTO.setIdx(x.getIdx());
                    subtaskDTO.setFileUrlList(fileUrlList);
                    subtaskDTO.setImageUrlList(imageUrlList);
                    subtaskDTO.setLinkUrlList(linkUrlList);

                    return subtaskDTO;
                })
                // 按 subtask 下标排序
                .sorted(Comparator.comparing(SubtaskDTO::getIdx))
                .collect(Collectors.toList());
    }

    public void checkSubtaskList(List<SubtaskParamDTO> subtaskList) {
        // 0.保证数目正常
        if (subtaskList.size() > 10000) {
            throw new ApiException(ResultCode.BAD_REQUEST, "subtask数目太多了");
        }

        // 1.保证idx连续, 且不重复, 且从0开始;
        int[] cnt = new int[10010];
        for (SubtaskParamDTO x : subtaskList) {
            if (x.getIdx() >= subtaskList.size() || cnt[x.getIdx()]++ == 1) {
                throw new ApiException(ResultCode.BAD_REQUEST, "idx不对劲啊");
            }
        }
        if (cnt[0] != 1) {
            throw new ApiException(ResultCode.BAD_REQUEST, "idx不对劲啊");
        }

        // 2.保证pidx在合法区间
        Arrays.fill(cnt, 0);
        for (SubtaskParamDTO x : subtaskList) {
            if (x.getPidx() == null) {
                throw new ApiException(ResultCode.BAD_REQUEST, "Pidx不能为空");
            }
            if (x.getPidx() == -1) {
                continue;
            }
            if (x.getPidx() >= subtaskList.size()) {
                throw new ApiException(ResultCode.BAD_REQUEST, "pidx不对劲啊");
            }

            cnt[x.getPidx()]++;
        }

        // 3.不成环; 从每个节点出发, 均可到达 root(-1), 并且layer正确
        for (SubtaskParamDTO x : subtaskList) {
            Arrays.fill(cnt, 0);
            int layerCnt = 0;
            int root = x.getIdx();
            while (root != -1) {
                if (cnt[root]++ == 1) {
                    throw new ApiException(ResultCode.BAD_REQUEST, "subtask结构不对劲啊");
                }

                layerCnt++;
                root = subtaskList.get(root).getPidx();
            }
            // 由于 layer 从0开始, 因此需要 -1 除去虚根那层
            if (layerCnt - 1 != x.getLayer()) {
                throw new ApiException(ResultCode.BAD_REQUEST, "layer不对劲啊");
            }
        }
    }

    /**
     * 写入结构信息
     */
    private void assignSubtaskStructure(List<Subtask> subtaskList, List<SubtaskParamDTO> subtaskParamDTOList) {
        // 写入 pid
        for (int i = 0; i < subtaskList.size(); i++) {
            SubtaskParamDTO subtaskParamDTO = subtaskParamDTOList.get(i);
            // 从 subtaskParamDTOList 获取 pidx(父节点下标)
            Integer pidx = subtaskParamDTO.getPidx();

            // 如果是根就特殊处理
            if (pidx == -1) {
                subtaskList.get(i).setPid(-1L);
            } else {
                subtaskList.get(i).setPid(subtaskList.get(pidx).getId());
            }
        }

        // 获取节点入度
        int[] cnt = new int[10010];
        subtaskParamDTOList.forEach(x -> {
            Integer pidx = x.getPidx();
            if (pidx != -1) {
                cnt[pidx]++;
            }
        });
        subtaskList.forEach(x -> x.setIsParent(cnt[x.getIdx()] != 0));
    }

}
