package com.wjs.examfrog.other.controller;


import com.wjs.examfrog.common.CommonPage;
import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.CategoryDTO;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.other.service.CategoryService;
import com.wjs.examfrog.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "Category 的 API")
@RestController()
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @ApiOperation(value = "获取全部", notes = "获取全部 分类")
    @RequestMapping(path = "/category/listAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CategoryVO>>> listAllCategories() {
        List<CategoryVO> categoryVOList = categoryService.listAllCategories();
        return CommonResult.success(categoryVOList);
    }

    @ApiOperation(value = "通过 分页 获取", notes = "按照分页获取 分类")
    @RequestMapping(path = "/category/list", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<CategoryVO>>> listCategories(@Valid PageParamDTO pageParamDTO) {
        // @Valid用于验证是否符合要求

        return CommonResult.success(CommonPage.restPage(categoryService.listCategories(pageParamDTO)));
    }

    @ApiOperation(value = "后台管理 页签下线")
    @PutMapping(path = "/category/remove/{categoryId}/{userId}/{areaId}")
    public ResponseEntity<Result<String>> removeCategory(@ApiParam(value = "标签的id",required = true) @PathVariable("categoryId") Long categoryId,
                                                         @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId,
                                                         @ApiParam(value = "分区的id",required = true) @PathVariable("areaId") Long areaId){
        categoryService.removeCategory(userId,categoryId,areaId);
        return CommonResult.noContent("下线成功");
    }

    @ApiOperation(value = "后台管理 页签上线")
    @PutMapping(path = "/category/online/{categoryId}/{userId}/{areaId}")
    public ResponseEntity<Result<String>> onlineCategory(@ApiParam(value = "标签的id",required = true) @PathVariable("categoryId") Long categoryId,
                                                         @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId,
                                                         @ApiParam(value = "分区的id",required = true) @PathVariable("areaId") Long areaId){
        categoryService.onlineCategory(userId,categoryId,areaId);
        return CommonResult.noContent("上线成功");
    }

    @ApiOperation(value = "后台管理 编辑页签")
    @PutMapping(path = "/category/edit/remove/{userId}")
    public ResponseEntity<Result<String>> editCategory(@RequestBody @Valid CategoryVO categoryVO,
                                                       @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId){
        categoryService.editCategory(userId,categoryVO);
        return CommonResult.noContent("编辑成功");
    }

    @ApiOperation(value = "后台管理 移动页签")
    @PutMapping(path = "/category/move/{categoryId}/{userId}/{moveType}/{areaId}")
    public ResponseEntity<Result<String>> moveCategory(@ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId,
                                                       @ApiParam(value = "标签的id",required = true) @PathVariable("categoryId") Long categoryId,
                                                       @ApiParam(value = "移动的类型 0:上移 1:下移",required = true) @PathVariable("moveType") Long moveType,
                                                       @ApiParam(value = "分区的id",required = true) @PathVariable("areaId") Long areaId){
        categoryService.moveCategory(userId,categoryId,moveType,areaId);
        return CommonResult.noContent("移动成功");
    }

    @ApiOperation(value = "后台管理 获取页签")
    @RequestMapping(path = "/category/manage/list", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<CategoryVO>>> listForManage(@Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(categoryService.listForManage(pageParamDTO)));
    }

    @ApiOperation(value = "后台管理 搜索页签")
    @RequestMapping(path = "/category/manage/search", method = RequestMethod.POST)
    public ResponseEntity<Result<CommonPage<CategoryVO>>> searchForManage(@Valid PageParamDTO pageParamDTO,
                                                                          @Valid @RequestBody CategoryDTO categoryDTO) {
        return CommonResult.success(CommonPage.restPage(categoryService.searchForManage(pageParamDTO,categoryDTO.getAreaId(), categoryDTO.getCategoryName(), categoryDTO.getStatus())));
    }

    @ApiOperation(value = "后台管理 新增页签")
    @RequestMapping(path = "/category/manage/create", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> createForManage(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.createForManage(categoryDTO);
        return CommonResult.noContent("保存成功");
    }
}

