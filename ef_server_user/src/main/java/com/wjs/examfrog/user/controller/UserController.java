package com.wjs.examfrog.user.controller;

import com.wjs.examfrog.common.CommonPage;
import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.PlanningParamDTO;
import com.wjs.examfrog.dto.UserParamDTO;
import com.wjs.examfrog.dto.UserPostParamDTO;
import com.wjs.examfrog.user.service.UserService;
import com.wjs.examfrog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


@Api(tags = "User 的 API")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * =========================用户===========================
     */
    @ApiOperation(value = "获取 用户信息")
    @RequestMapping(path = "/portal/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Result<UserDetailsVO>> detail(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId) {
        UserDetailsVO userDetailsVO = userService.detail(userId);
        return CommonResult.success(userDetailsVO);
    }


    /**
     * ==========================收藏==========================
     */
    @ApiOperation(value = "获取 收藏帖子", notes = "分页, 按时间倒序返回")
    @RequestMapping(path = "/{userId}/userpost/list", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostVO>>> listUserPosts(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                                        @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listFavUserPosts(userId, pageParamDTO)));
    }


    @ApiOperation(value = "获取 收藏用户帖子", notes = "分页, 按时间倒序返回")
    @RequestMapping(path = "/{userId}/fav/userpost/list", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserPostVO>>> listFavUserPosts(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                                     @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listFavUserPosts(userId, pageParamDTO)));
    }

    @ApiOperation(value = "添加 收藏用户帖子")
    @RequestMapping(path = "/{userId}/fav/userpost", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> saveFavUserPost(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                          @ApiParam(value = "用户帖子Id", required = true) @RequestParam(value = "userPostId") Long userPostId,
                                                          @ApiParam(value = "文件夹id(根目录-1)", required = true) @RequestParam(value = "favPostFolderId") Long favPostFolderId) {
        userService.saveFavUserPost(userId, userPostId, favPostFolderId);
        return CommonResult.created("收藏成功");
    }

    @ApiOperation(value = "删除多个 收藏用户帖子")
    @RequestMapping(path = "/{userId}/fav/userpost", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removeFavUserPosts(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                             @ApiParam(value = "用户帖子Id", required = true) @RequestParam(value = "userPostIdList") List<Long> userPostIdList) {
        userService.removeFavUserPosts(userId, userPostIdList);
        return CommonResult.noContent("取消收藏成功");
    }

    /**
     * ===============================关注================================
     */
    @ApiOperation(value = "获取 关注", notes = "分页, 按时间倒序返回")
    @RequestMapping(path = "/{userId}/follow/follow", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserVO>>> listFollows(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                            @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listFollows(userId, pageParamDTO)));
    }

    @ApiOperation(value = "获取 粉丝", notes = "分页, 按时间倒序返回")
    @RequestMapping(path = "/{userId}/follow/fans", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserVO>>> listFans(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                         @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listFans(userId, pageParamDTO)));
    }

    @ApiOperation(value = "添加 关注用户")
    @RequestMapping(path = "/{userId}/follow", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> saveFollowUser(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                         @ApiParam(value = "用户Id", required = true) @RequestParam(value = "followId") Long followId) {
        userService.saveFollowUser(userId, followId);
        return CommonResult.created("关注成功");
    }

    @ApiOperation(value = "删除多个 关注用户")
    @RequestMapping(path = "/{userId}/follow", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removeFollowUsers(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                            @ApiParam(value = "用户Id", required = true) @RequestParam(value = "followId") List<Long> followIdList) {
        userService.removeFollowUsers(userId, followIdList);
        return CommonResult.noContent("取消关注成功");
    }

    /**
     * ==============================用户分享帖子=================================
     */
    @ApiOperation(value = "发布 用户帖子")
    @RequestMapping(path = "/{userId}/userpost", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> publishUserPost(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                          @RequestBody @Valid UserPostParamDTO userPostParamDTO) {
        userService.publishUserPost(userId, userPostParamDTO);
        return CommonResult.created("添加成功");
    }

    @ApiOperation(value = "更新 用户帖子")
    @RequestMapping(path = "/{userId}/userpost/{userPostId}", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> updateUserPost(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                         @ApiParam(value = "用户帖子 id", required = true) @PathVariable(value = "userPostId") Long userPostId,
                                                         @RequestBody @Valid UserPostParamDTO userPostParamDTO) {
        userService.updateUserPost(userId, userPostId, userPostParamDTO);
        return CommonResult.noContent("更新成功");
    }

    @ApiOperation(value = "删除 用户帖子")
    @RequestMapping(path = "/{userId}/userpost", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removeUserPosts(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                          @ApiParam(value = "用户帖子 id列表", required = true) @RequestParam List<Long> userPostIdList) {
        userService.removeUserPosts(userId, userPostIdList);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "点赞 用户帖子")
    @RequestMapping(path = "/{userId}/like", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> saveLikeUserPost(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                           @ApiParam(value = "用户帖子Id", required = true) @RequestParam(value = "userPostId") Long userPostId) {
        userService.saveLikeUserPost(userId, userPostId);
        return CommonResult.noContent("点赞成功");
    }

    @ApiOperation(value = "取消点赞 用户帖子")
    @RequestMapping(path = "/{userId}/like", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removeLikeUserPost(@ApiParam(value = "用户id", required = true) @PathVariable(value = "userId") Long userId,
                                                             @ApiParam(value = "用户帖子Id", required = true) @RequestParam(value = "userPostId") Long userPostId) {
        userService.removeLikeUserPost(userId, userPostId);
        return CommonResult.noContent("取消点赞成功");
    }

    /**
     * =================================subtask=================================
     */
    @ApiOperation(value = "完成 目标")
    @RequestMapping(path = "/{userId}/file/planningFolder/{planningId}/{subtaskId}/bingo", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> bingo(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                @ApiParam(value = "用户规划 id", required = true) @PathVariable(value = "planningId") Long planningId,
                                                @ApiParam(value = "目标 id", required = true) @PathVariable(value = "subtaskId") Long subtaskId) {
        userService.bingo(userId, planningId, subtaskId);
        return CommonResult.noContent("更新成功");
    }

    @ApiOperation(value = "取消完成 目标")
    @RequestMapping(path = "/{userId}/file/planningFolder/{planningId}/{subtaskId}/nbingo", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> cancelBingo(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                      @ApiParam(value = "用户规划 id", required = true) @PathVariable(value = "planningId") Long planningId,
                                                      @ApiParam(value = "目标 id", required = true) @PathVariable(value = "subtaskId") Long subtaskId) {
        userService.cancelBingo(userId, planningId, subtaskId);
        return CommonResult.noContent("更新成功");
    }


    /**
     * ==============================规划文件夹==============================
     */


    @ApiOperation(value = "获取 规划文件夹")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/listPlanningFolder", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<PlanningFolderVO>>> listPlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                              @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                                              @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listPlanningFolders(userId, planningFolderId, pageParamDTO)));
    }

    @ApiOperation(value = "添加 规划文件夹")
    @RequestMapping(path = "/{userId}/file/{pid}", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> savePlanningFolder(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                             @ApiParam(value = "文件夹 pid(根目录填写 -1)", required = true) @PathVariable(value = "pid") Long pid,
                                                             @ApiParam(value = "文件夹 名称", required = true) @RequestParam String planningFolderName) {
        userService.savePlanningFolder(userId, planningFolderName, pid);
        return CommonResult.noContent("创建成功");
    }

    @ApiOperation(value = "更新 规划文件夹")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> updatePlanningFolder(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                               @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                               @ApiParam(value = "文件夹 名称", required = true) @RequestParam String planningFolderName) {
        userService.updatePlanningFolder(userId, planningFolderId, planningFolderName);
        return CommonResult.noContent("更新成功");
    }

    @ApiOperation(value = "删除多个 规划文件夹")
    @RequestMapping(path = "/{userId}/file/planningFolder", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removePlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                @ApiParam(value = "文件夹 id列表", required = true) @RequestParam List<Long> planningFolderIdList) {
        userService.removePlanningFolders(userId, planningFolderIdList);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "移动 规划文件夹")
    @RequestMapping(path = "/{userId}/file/planningFolder/move", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> movePlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                              @ApiParam(value = "文件夹 id列表", required = true) @RequestParam List<Long> planningFolderIdList,
                                                              @ApiParam(value = "目标文件夹 id(根目录填写 -1)", required = true) @RequestParam Long targetPlanningFolderId) {
        userService.movePlanningFolders(userId, planningFolderIdList, targetPlanningFolderId);
        return CommonResult.noContent("移动成功");
    }

    /**
     * ==============================规划=================================
     */
    @ApiOperation(value = "获取详情 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/{planningId}", method = RequestMethod.GET)
    public ResponseEntity<Result<PlanningDetailsVO>> getPlanningDetail(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                       @ApiParam(value = "文件夹 id", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                                       @ApiParam(value = "规划 id", required = true) @PathVariable(value = "planningId") Long planningId) {
        PlanningDetailsVO planningDetailsVO = userService.getPlanningDetail(userId, planningFolderId, planningId);
        return CommonResult.success(planningDetailsVO);
    }

    @ApiOperation(value = "获取 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/listPlanning", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<PlanningVO>>> listPlannings(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                  @ApiParam(value = "文件夹 id", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                                  @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listPlannings(userId, planningFolderId, pageParamDTO)));
    }

    @ApiOperation(value = "添加 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/planning", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> savePlanning(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                       @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                       @ApiParam(value = "用户规划实体", required = true) @RequestBody @Valid PlanningParamDTO planningParamDTO) {
        userService.savePlanning(userId, planningFolderId, planningParamDTO);
        return CommonResult.created("添加成功");
    }

    @ApiOperation(value = "更新 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/{planningId}", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> updatePlanning(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                         @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                         @ApiParam(value = "用户规划实体 id", required = true) @PathVariable(value = "planningId") Long planningId,
                                                         @ApiParam(value = "用户规划实体", required = true) @RequestBody @Valid PlanningParamDTO planningParamDTO) {
        userService.updatePlanning(userId, planningFolderId, planningId, planningParamDTO);
        return CommonResult.noContent("更新成功");
    }

    @ApiOperation(value = "删除多个 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/planning", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removePlannings(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                          @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                          @ApiParam(value = "规划 id列表", required = true) @RequestParam List<Long> planningIdList) {
        userService.removePlannings(userId, planningFolderId, planningIdList);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "移动 规划")
    @RequestMapping(path = "/{userId}/file/{planningFolderId}/planning/move", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> movePlannings(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                        @ApiParam(value = "文件夹 id(根目录填写 -1)", required = true) @PathVariable(value = "planningFolderId") Long planningFolderId,
                                                        @ApiParam(value = "规划 id列表", required = true) @RequestParam List<Long> planningIdList,
                                                        @ApiParam(value = "目标文件夹 id(根目录填写 -1)", required = true) @RequestParam Long targetPlanningFolderId) {
        userService.movePlannings(userId, planningFolderId, planningIdList, targetPlanningFolderId);
        return CommonResult.noContent("移动成功");
    }

    /**
     * =============================回收站=============================
     */

    @ApiOperation(value = "获取 已删除的 规划文件夹")
    @RequestMapping(path = "/{userId}/deleted/planningFolder", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<PlanningFolderVO>>> listDeletedPlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                                     @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listDeletedPlanningFolders(userId, pageParamDTO)));
    }

    @ApiOperation(value = "恢复 已删除的 规划文件夹")
    @RequestMapping(path = "/{userId}/deleted/planningFolder", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> recoveryDeletedPlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                         @ApiParam(value = "文件夹 id列表", required = true) @RequestParam(value = "planningFolderIdList") List<Long> planningFolderIdList) {
        userService.recoveryDeletedPlanningFolders(userId, planningFolderIdList);
        return CommonResult.noContent("恢复成功");
    }

    @ApiOperation(value = "彻底删除 已删除的 规划文件夹")
    @RequestMapping(path = "/{userId}/deleted/planningFolder", method = RequestMethod.DELETE)
    public ResponseEntity<Result<List<PlanningFolderVO>>> removeDeletedPlanningFolders(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                                       @ApiParam(value = "文件夹 id列表", required = true) @RequestParam(value = "planningFolderIdList") List<Long> planningFolderIdList) {
        userService.removeDeletedPlanningFolders(userId, planningFolderIdList);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "获取 已删除的 规划")
    @RequestMapping(path = "/{userId}/deleted/planning", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<PlanningVO>>> listDeletedUserPosts(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                         @Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listDeletedPlannings(userId, pageParamDTO)));
    }

    @ApiOperation(value = "恢复 已删除的 规划")
    @RequestMapping(path = "/{userId}/deleted/planning", method = RequestMethod.POST)
    public ResponseEntity<Result<String>> recoveryDeletedUserPosts(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                   @ApiParam(value = "规划 id列表", required = true) @RequestParam(value = "userPostIdList") List<Long> userPostIdList) {
        userService.recoveryDeletedPlannings(userId, userPostIdList);
        return CommonResult.noContent("恢复成功");
    }

    @ApiOperation(value = "彻底删除 已删除的 规划")
    @RequestMapping(path = "/{userId}/deleted/planning", method = RequestMethod.DELETE)
    public ResponseEntity<Result<List<UserPostVO>>> removeDeletedUserPosts(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId,
                                                                           @ApiParam(value = "规划 id列表", required = true) @RequestParam(value = "userPostIdList") List<Long> userPostIdList) {
        userService.removeDeletedUserPosts(userId, userPostIdList);
        return CommonResult.noContent("删除成功");
    }

    @ApiOperation(value = "彻底删除 全部")
    @RequestMapping(path = "/{userId}/deleted", method = RequestMethod.DELETE)
    public ResponseEntity<Result<String>> removeAllDeleted(@ApiParam(value = "用户 id", required = true) @PathVariable(value = "userId") Long userId) {
        userService.removeAllDeleted(userId);
        return CommonResult.noContent("删除成功");
    }

    /**
     * =============================后台管理=============================
     */

    @ApiOperation(value = "后台管理 获取用户列表")
    @RequestMapping(path = "/portal/manage/user/listAll", method = RequestMethod.GET)
    public ResponseEntity<Result<CommonPage<UserVO>>> listForManage(@Valid PageParamDTO pageParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listForManage(pageParamDTO)));
    }

    @ApiOperation(value = "后台管理 搜索用户")
    @RequestMapping(path = "/portal/manage/user/search", method = RequestMethod.POST)
    public ResponseEntity<Result<CommonPage<UserVO>>> listBySearch(@Valid PageParamDTO pageParamDTO,
                                                                   @RequestBody @Valid UserParamDTO userParamDTO) {
        return CommonResult.success(CommonPage.restPage(userService.listBySearch(pageParamDTO,userParamDTO)));
    }

    @ApiOperation(value = "后台管理 冻结/解冻用户")
    @RequestMapping(path = "/portal/manage/user/freeze", method = RequestMethod.PUT)
    public ResponseEntity<Result<String>> freezeUser(@ApiParam(value = "分类List，如果是多个，传多个id即可") @RequestParam(value = "idList",required = true) List<Long> idList,
                                                     @ApiParam(value = "操作类型，0是冻结，1是解冻") @RequestParam(value = "type",required = true) Long type) {
        userService.freezeUser(idList,type);
        return CommonResult.noContent("更改成功");
    }
}

