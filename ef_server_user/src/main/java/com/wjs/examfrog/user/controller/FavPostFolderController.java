package com.wjs.examfrog.user.controller;

import com.wjs.examfrog.common.CommonPage;
import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.entity.FavPostFolder;
import com.wjs.examfrog.user.service.UserService;
import com.wjs.examfrog.vo.FavPostFolderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "收藏 文件夹")
@RequestMapping("/favPost")
@RestController
public class FavPostFolderController {

    @Resource
    private UserService userService;

    /**
     * ==============================收藏文件夹==============================
     */

    @ApiOperation(value = "添加 收藏文件夹")
    @PostMapping("/{userId}/favPostFolder/saveFavPostFolder")
    public ResponseEntity<Result<String>> saveFavPostFolder(@ApiParam(value = "用户id", required = true) @PathVariable("userId") Long userId,
                                                            @RequestBody FavPostFolder favPostFolder) {
        userService.saveFavPostFolder(userId, favPostFolder);
        return CommonResult.noContent("创建成功");
    }

    /**
     * 获取文件夹的同时要判断是自己查看还是别人查看！
     * @param userId
     * @param favPostFolderId
     * @param pageParamDTO
     * @return
     */
    @ApiOperation(value = "获取 收藏文件夹")
    @GetMapping("/{userId}/favPostFolder/{favPostFolderId}/listFavPostFolder/{visitedId}")
    public ResponseEntity<Result<CommonPage<FavPostFolderVO>>> listFavPostFolders(@ApiParam(value = "用户id", required = true) @PathVariable("userId") Long userId,
                                                                                  @ApiParam(value = "文件夹 pid(根目录-1)", required = true) @PathVariable("favPostFolderId") Long favPostFolderId,
                                                                                  @ApiParam(value = "被访问者id", required = true) @PathVariable("visitedId") Long visitedId,
                                                                                  @Valid PageParamDTO pageParamDTO) {

        return CommonResult.success(CommonPage.restPage(userService.listFavPostFolders(userId, favPostFolderId, visitedId, pageParamDTO)));
    }

    @ApiOperation(value = "修改 收藏文件夹")
    @PutMapping("/{userId}/favPostFolder/update/{favPostFolderId}")
    public ResponseEntity<Result<String>> updateFavPostFolder(@ApiParam(value = "userId", required = true) @PathVariable("userId") Long userId,
                                                              @ApiParam(value = "favPostFolderId", required = true) @PathVariable("favPostFolderId") Long favPostFolderId,
                                                              @RequestBody FavPostFolder favPostFolder) {
        userService.updateFavPostFolder(userId, favPostFolder, favPostFolderId);
        return CommonResult.noContent("更新成功");
    }

    @ApiOperation(value = "移动 收藏文件夹")
    @PostMapping("/{userId}/favPostFolder/move/{targetId}")
    public ResponseEntity<Result<String>> moveFavPostFolder(@ApiParam(value = "userId", required = true) @PathVariable("userId") Long userId,
                                                            @ApiParam(value = "文件夹id列表", required = true) @RequestParam("favPostFolderId") List<Long> favPostFolderIdList,
                                                            @ApiParam(value = "目标文件夹id(根目录-1)", required = true) @PathVariable("targetId") Long targetId) {
        userService.moveFavPostFolder(userId, favPostFolderIdList, targetId);
        return CommonResult.noContent("移动成功");
    }


    @ApiOperation(value = "删除 收藏文件夹")
    @DeleteMapping("/{userId}/removeFavPostFolder")
    public ResponseEntity<Result<String>> removeFavPostFolder(@ApiParam(value = "userId", required = true) @PathVariable("userId") Long userId,
                                                              @ApiParam(value = "文件夹id列表", required = true) @RequestParam("favPostFolderId") List<Long> favPostFolderIdList) {
        userService.removeFavPostFolder(userId, favPostFolderIdList);
        return CommonResult.noContent("删除成功");
    }
}
