package com.wjs.examfrog.other.controller;


import com.wjs.examfrog.common.CommonPage;
import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PostManageDTO;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.other.service.UserPostService;
import com.wjs.examfrog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "UserPost 的 API")
@RequestMapping("/userPost")
@RestController
public class UserPostController {

    @Resource
    private UserPostService userPostService;

    @ApiOperation(value = "获取全部 用户帖子")
    @RequestMapping(path = "/listAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<UserPostVO>>> listAllUserPosts() {
        List<UserPostVO> userPostVOList = userPostService.listAllUserPosts();
        return CommonResult.success(userPostVOList);
    }

    @ApiOperation(value = "获取 用户帖子", notes = "通过 分页/分类 获取")
    @RequestMapping(path = "/listByType", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostVO>>> listUserPosts(@Valid PageParamDTO pageParamDTO,
                                                                        @ApiParam(value = "分类List") @RequestParam(required = false) List<Long> categoryIdList) {
        return CommonResult.success(CommonPage.restPage(userPostService.listUserPosts(categoryIdList, pageParamDTO)));
    }

    @ApiOperation(value = "获取 个人帖子 通过类型筛选", notes = "通过userID")
    @RequestMapping(path = "/byuserid/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostVO>>> listUserPostsById(@Valid PageParamDTO pageParamDTO,
                                                                            @ApiParam(value = "用户id",required = true) @PathVariable(value ="userId") Long userId,
                                                                            @ApiParam(value = "排序类型，0：按发布时间 1：按阅读数量 2：按使用次数",required = true)@RequestParam(value = "orderType") int orderType){
        return CommonResult.success(CommonPage.restPage(userPostService.listUserPostById(pageParamDTO,userId,orderType)));
    }

    @ApiOperation(value = "获取详情 用户帖子")
    @RequestMapping(path = "/{userPostId}", method = RequestMethod.GET)
    public ResponseEntity<Result<UserPostDetailsVO>> detail(@ApiParam(value = "用户帖子 id", required = true) @PathVariable(value = "userPostId") Long userPostId) {
        UserPostDetailsVO userPostDetailsVO = userPostService.detail(userPostId);
        return CommonResult.success(userPostDetailsVO);
    }


    @ApiOperation(value = "获取热榜", notes = "按照近3天点击量返回 用户帖子列表")
    @RequestMapping(path = "/hotlist", method = RequestMethod.GET)
    public ResponseEntity<Result<List<UserPostVO>>> getHotList(@ApiParam(value = "热榜前size", required = true) @RequestParam(value = "size") Long size) {
        List<UserPostVO> userPostVOList = userPostService.getHotList(size);
        return CommonResult.success(userPostVOList);
    }

    @ApiOperation(value = "搜索", notes = "搜索")
    @RequestMapping(path = "/query", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostVO>>> listUserPostsByQuery(@Valid PageParamDTO pageParamDTO,
                                                                               @ApiParam(value = "关键字", required = true) @RequestParam(value = "words") String words) {
        return CommonResult.success(CommonPage.restPage(userPostService.listUserPostsByQuery(pageParamDTO, words)));
    }

    @ApiOperation(value = "后台管理 用户文章管理")
    @RequestMapping(path = "/manage/get",method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostManageVO>>> listByManage(@Valid PageParamDTO pageParamDTO){
        return CommonResult.success(CommonPage.restPage(userPostService.listByManage(pageParamDTO)));
    }

    @ApiOperation(value = "后台管理 用户文章详情")
    @GetMapping(path = "/manage/detail/{id}")
    public ResponseEntity<Result<UserPostManageDetailsVO>> manageDetail(@ApiParam(value = "用户帖子id",required = true) @PathVariable("id") Long postId){
        UserPostManageDetailsVO userPostManageDetailsVO = userPostService.manageDetail(postId);
        return CommonResult.success(userPostManageDetailsVO);
    }

    @ApiOperation(value = "后台管理 上线用户帖子")
    @RequestMapping(path = "/manage/online/{userPostId}/{userId}",method = RequestMethod.POST)
    public ResponseEntity<Result<String>> onlineUserPost(@ApiParam(value = "用户帖子id",required = true) @PathVariable("userPostId") Long userPostId,
                                                         @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId){
        userPostService.onlineUserPost(userId,userPostId);
        return CommonResult.noContent("上线成功");
    }

    @ApiOperation(value = "后台管理 下线用户帖子")
    @RequestMapping(path = "/manage/remove/{userPostId}/{userId}",method = RequestMethod.POST)
    public ResponseEntity<Result<String>> removeUserPostForManage(@ApiParam(value = "用户帖子id",required = true) @PathVariable("userPostId") Long userPostId,
                                                         @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId){
        userPostService.removeUserPostForManage(userId,userPostId);
        return CommonResult.noContent("下线成功");
    }

    @ApiOperation(value = "后台管理 删除用户帖子")
    @RequestMapping(path = "/manage/delete/{userPostId}/{userId}",method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> deleteUserPostForManage(@ApiParam(value = "用户帖子id",required = true) @PathVariable("userPostId") Long userPostId,
                                                         @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId){
        userPostService.deleteUserPostForManage(userId,userPostId);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "后台管理 搜索用户文章")
    @PostMapping(path = "/manage/search")
    public ResponseEntity<Result<CommonPage<UserPostManageVO>>> listBySearch(@Valid PageParamDTO pageParamDTO,
                                                                             @RequestBody @Valid PostManageDTO postManageDTO){
        return CommonResult.success(CommonPage.restPage(userPostService.listBySearch(pageParamDTO, postManageDTO)));
    }

    @ApiOperation(value = "后台管理 牛蛙内容")
    @RequestMapping(path = "/excellent/manage",method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<ExcellentPostVO>>> listExcellentByManage(@Valid PageParamDTO pageParamDTO){
        return CommonResult.success(CommonPage.restPage(userPostService.listExcellentByManage(pageParamDTO)));
    }

    @ApiOperation(value = "后台管理 操作牛蛙内容")
    @PostMapping(path = "/excellent/{type}/{id}/{userId}")
    public ResponseEntity<Result<String>> controlStatus(@ApiParam(value = "牛蛙内容id",required = true) @PathVariable("id") Long id,
                                                        @ApiParam(value = "操作类型 0:通过/上线  1:下线  2:驳回") @PathVariable("type") Long type,
                                                        @ApiParam(value = "用户id",required = true) @PathVariable("userId") Long userId){
        userPostService.controlStatus(userId,id,type);
        return CommonResult.noContent("操作成功");
    }

    @ApiOperation(value = "获取用户自己的帖子")
    @GetMapping(path = "/userPost/listById/{userId}")
    public ResponseEntity<Result<List<UserPost>>> list(@PathVariable Long userId){
        return CommonResult.success(userPostService.listUserPostByUserId(userId));
    }
}

