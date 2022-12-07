package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.CategoryDTO;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.Area;
import com.wjs.examfrog.entity.Category;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.mapper.*;
import com.wjs.examfrog.other.service.CategoryService;
import com.wjs.examfrog.other.service.ApiService;
import com.wjs.examfrog.util.CreateIdUtil;
import com.wjs.examfrog.vo.CategoryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    ApiService apiService;
    @Resource
    AreaMapper areaMapper;
    @Resource
    UserPostMapper userPostMapper;
    @Resource
    private SelfIdGenerator selfIdGenerator;

    @Override
    public List<CategoryVO> listAllCategories() {
        // 查询
        List<Category> categoryList = this.list(null);

        return this.convertCategoryVOs(categoryList);
    }

    @Override
    public Page listCategories(PageParamDTO pageParamDTO) {
        // 分页
        Page<Category> categoryPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 查询
        Page<Category> page = this.page(categoryPage);

        return new Page<CategoryVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertCategoryVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void removeCategory(Long userId, Long categoryId,Long areaId) {
        apiService.checkOperator(userId);

        Category category = categoryMapper.getOneByManage(categoryId,areaId);
        if (category == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"下线的标签不存在");
        } else if (category.getIsDelete()) {
            throw new ApiException(ResultCode.BAD_REQUEST,"此标签已下线，无需重复操作");
        }

        //将后面标签的位置往移动一位
        Long position = category.getPosition();
        Long maxPosition = categoryMapper.countMaxPosition(areaId);

        category.setPosition(CreateIdUtil.creatId());
        if(!categoryMapper.removeCategory(category)){
            throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
        }

        if (!position.equals(maxPosition)) {
            for (long i = position+1; i <= maxPosition; i++) {
                if (!categoryMapper.updatePosition(i - 1, i,areaId)) {
                    throw new ApiException(ResultCode.BAD_REQUEST, "删除失败");
                }
            }
        }
    }

    @Override
    public void onlineCategory(Long userId, Long categoryId,Long areaId) {
        apiService.checkOperator(userId);

        //查找
        Category category = categoryMapper.getOneByManage(categoryId,areaId);

        if (category == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "要上线的标签不存在");
        }

        //新上线的标签移动到最后
        if (!categoryMapper.updatePosition(categoryMapper.countMaxPosition(areaId)+1,category.getPosition(), areaId)) {
            throw new ApiException(ResultCode.BAD_REQUEST, "上线失败");
        }

        //将标签上线
        boolean res = categoryMapper.onlineCategory(categoryId);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "上线失败");
        }
    }

    @Override
    public void editCategory(Long userId, CategoryVO categoryVO) {
        apiService.checkOperator(userId);

        if (categoryVO.getAreaId() == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"分区id不能为空");
        }

        Category category = categoryMapper.getOneByManage(categoryVO.getId(),categoryVO.getAreaId());
        if (category == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "编辑的标签不存在");
        }

        boolean res = categoryMapper.editCategory(categoryVO);

        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST, "编辑失败");
        }
    }

    @Override
    public void moveCategory(Long userId, Long categoryId, Long moveType,Long areaId) {
        apiService.checkOperator(userId);

        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>()
                                                          .eq("id",categoryId)
                                                          .eq("area_id",areaId);
        Category category = this.getOne(queryWrapper);
        if (category == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"移动的标签已下线或不存在");
        }

        //交换位置的category
        Category change;
        Long position;

        position = category.getPosition();
        if (moveType == 0) {
            if (position == 0) {
                throw new ApiException(ResultCode.BAD_REQUEST, "已是最前位置");
            }

            queryWrapper = new QueryWrapper<Category>().eq("position",position-1)
                                                                   .eq("area_id",areaId);
            change = this.getOne(queryWrapper);
            change.setPosition(position);
            if (!this.updateById(change)) {
                throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
            }

            category.setPosition(position - 1);
            if(!this.updateById(category)){
                throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
            }
        } else if (moveType == 1) {
            Long maxPosition = categoryMapper.countMaxPosition(areaId);

            if (position == maxPosition) {
                throw new ApiException(ResultCode.BAD_REQUEST, "已是最后位置");
            }

            queryWrapper = new QueryWrapper<Category>().eq("position",position+1)
                                                                   .eq("area_id",areaId);
            change = this.getOne(queryWrapper);
            change.setPosition(position);
            if (!this.updateById(change)) {
                throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
            }

            category.setPosition(position + 1);
            if(!this.updateById(category)){
                throw new ApiException(ResultCode.BAD_REQUEST, "移动失败");
            }
        } else {
            throw new ApiException(ResultCode.BAD_REQUEST, "移动类型不合法");
        }
    }

    @Override
    public Page<CategoryVO> listForManage(PageParamDTO pageParamDTO) {
        Page<Category> page = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        page = categoryMapper.listForManage(page);
        List<CategoryVO> list = this.convertCategoryVOs(page.getRecords());

        return new Page<CategoryVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setTotal(page.getTotal())
                .setRecords(list);
    }

    @Override
    public Page<CategoryVO> searchForManage(PageParamDTO pageParamDTO, Long areaId, String categoryName, Long status) {
        if (status != null && (status < 0 || status > 2)) {
            throw new ApiException(ResultCode.BAD_REQUEST,"查询的状态不对呀");
        }

        Page<Category> page = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        Long categoryId = null;

        page = categoryMapper.searchForManage(page,categoryName,areaId,categoryId,status);

        return new Page<CategoryVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertCategoryVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void createForManage(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryName() == null || "".equals(categoryDTO.getCategoryName())){
            throw new ApiException(ResultCode.BAD_REQUEST,"标签名称不合法");
        }

        if (categoryDTO.getAreaId() == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"没有选择分区！");
        }

        Category category = new Category();
        category.setName(categoryDTO.getCategoryName());
        category.setAreaId(categoryDTO.getAreaId());
        //要设置新保存的标签的下标
        Long maxPosition = categoryMapper.countMaxPosition(categoryDTO.getAreaId());
        category.setPosition(maxPosition+1);
        category.setId(selfIdGenerator.nextId(null));

        boolean res = this.save(category);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST,"保存失败");
        }
    }


    public List<CategoryVO> convertCategoryVOs(List<Category> categoryList) {
        return categoryList.parallelStream()
                .map(this::convertCategoryVO)
                .collect(Collectors.toList());
    }


    public CategoryVO convertCategoryVO(Category category) {
        // 构建 CategoryVO
        CategoryVO categoryVO = new CategoryVO();
        BeanUtil.copyProperties(category, categoryVO);

        Area area = areaMapper.getOneForManage(category.getAreaId());
        if (area == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"标签所在的分区不存在");
        }
        categoryVO.setAreaName(area.getName());

        categoryVO.setCount(userPostMapper.countNum(category.getAreaId(),category.getId()));

        return categoryVO;
    }

}
