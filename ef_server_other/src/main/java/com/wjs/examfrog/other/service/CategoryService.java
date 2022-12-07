package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.dto.CategoryDTO;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.Category;
import com.wjs.examfrog.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 查询所有
     *
     * @return
     */
    List<CategoryVO> listAllCategories();

    /**
     * 分页查询
     *
     * @param pageParamDTO
     * @return
     */
    Page listCategories(PageParamDTO pageParamDTO);


    /**
     * 后台管理 下线页签
     * @param categoryId
     */
    void removeCategory(Long userId,Long categoryId,Long areaId);

    /**
     * 后台管理 上线页签
     * @param categoryId
     */
    void onlineCategory(Long userId,Long categoryId,Long areaId);

    /**
     * 后台管理 编辑页签
     * @param categoryVO
     */
    void editCategory(Long userId,CategoryVO categoryVO);

    /**
     * 后台管理 移动页签
     * @param userId
     * @param categoryId
     * @param moveType
     */
    void moveCategory(Long userId,Long categoryId,Long moveType,Long areaId);

    /**
     * 后台管理 获取页签
     * @param pageParamDTO
     * @return
     */
    Page<CategoryVO> listForManage(PageParamDTO pageParamDTO);

    /**
     * 后台管理 搜索页签
     * @param pageParamDTO
     * @param areaId
     * @param categoryName
     * @param status
     * @return
     */
    Page<CategoryVO> searchForManage(PageParamDTO pageParamDTO,Long areaId,String categoryName,Long status);

    /**
     * 后台管理 新增页签
     * @param categoryDTO
     */
    void createForManage(CategoryDTO categoryDTO);
}
