package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.entity.Category;
import com.wjs.examfrog.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;


public interface CategoryMapper extends BaseMapper<Category> {
    boolean onlineCategory(Long categoryId);

    boolean removeCategory(Category category);

    boolean editCategory(CategoryVO categoryVO);

    Category getOneByManage(@Param("categoryId") Long categoryId, @Param("areaId") Long areaId);

    Long countMaxPosition(Long areaId);

    Boolean updatePosition(@Param("new") Long newPosition,
                           @Param("position") Long position,
                           @Param("areaId") Long areaId);

    Page<Category> listForManage(Page<Category> page);

    Page<Category> searchForManage(Page<Category> page,
                                   @Param("name")String name,
                                   @Param("areaId") Long areaId,
                                   @Param("categoryId") Long categoryId,
                                   @Param("status") Long status);
}
