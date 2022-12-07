package com.wjs.examfrog.other.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.SubtaskParamDTO;
import com.wjs.examfrog.dto.UserPostParamDTO;
import com.wjs.examfrog.entity.Category;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.other.service.CategoryService;
import com.wjs.examfrog.other.service.FavUserPostService;
import com.wjs.examfrog.other.service.SubtaskService;
import com.wjs.examfrog.other.service.UserPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ApiController {
    @Resource
    private UserPostService userPostService;

    @Resource
    private SubtaskService subtaskService;

    @Resource
    private FavUserPostService favUserPostService;

    @Resource
    private CategoryService categoryService;

    //******************USERPOST****************
    @PostMapping(path = "/api/userPost/publish/{userId}")
    public ResponseEntity<Result<Boolean>> publishUserPost(@PathVariable("userId") Long userId, @RequestBody UserPostParamDTO userPostParamDTO){
        return CommonResult.success(userPostService.publishUserPost(userId,userPostParamDTO));
    }


    @PutMapping(path = "/api/userPost/update/{userId}/{userPostId}")
    public ResponseEntity<Result<Boolean>> updateUserPost(@PathVariable("userId") Long userId,@PathVariable("userPostId") Long userPostId,@RequestBody UserPostParamDTO userPostParamDTO){
        return CommonResult.success(userPostService.updateUserPost(userId,userPostId,userPostParamDTO));
    }

    @DeleteMapping(path = "/api/userPost/remove/{userId}")
    public ResponseEntity<Result<Boolean>> removeUserPosts(@PathVariable("userId") Long userId, @RequestBody List<Long> userPostIdList){
        return CommonResult.success(userPostService.removeUserPosts(userId,userPostIdList));
    }

    @GetMapping(path = "/api/userPost/listById/{userId}")
    public ResponseEntity<Result<List<UserPost>>> listUserPost(@PathVariable("userId") Long userId){
        QueryWrapper<UserPost> wrapper = new QueryWrapper<UserPost>().eq("author_id", userId);
        return CommonResult.success(userPostService.list(wrapper));
    }

    @GetMapping(path = "/api/userPost/getPostById/{userPostId}")
    public ResponseEntity<Result<UserPost>> getPostById(@PathVariable("userPostId") Long userPostId){
        return CommonResult.success(userPostService.getOneById(userPostId));
    }

    @PutMapping("/api/userPost/updateOneById")
    public ResponseEntity<Result<Boolean>> updateOneById(@RequestBody UserPost userPost){
        return CommonResult.success(userPostService.updateOneById(userPost));
    }

    @PutMapping("/api/userPost/increaseSuccessCount/{userPostId}")
    public ResponseEntity<Result<Boolean>> increaseSuccessCount(@PathVariable("userPostId") Long userPostId){
        return CommonResult.success(userPostService.increaseSuccessCount(userPostId));
    }

    @PutMapping("/api/userPost/listFavUserPosts/{userId}")
    public ResponseEntity<Result<Page>> listFavUserPosts(@PathVariable("userId") Long userId, @RequestBody PageParamDTO pageParamDTO){
        return CommonResult.success(userPostService.listFavUserPosts(userId,pageParamDTO));
    }

    @GetMapping("/api/userPost/listPostByIds")
    public ResponseEntity<Result<List<UserPost>>> listPostByIds(@RequestBody List<Long> ids){
        return CommonResult.success(userPostService.listByIds(ids));
    }

    @GetMapping("/api/userPost/updateBatchPostById")
    public ResponseEntity<Result<Boolean>> updateBatchPostById(@RequestBody List<UserPost> userPostList){
        return CommonResult.success(userPostService.updateBatchById(userPostList));
    }

    @PutMapping("api/userPost/saveFavUserPost/{userId}/{userPostId}/{favPostFolderId}")
    public ResponseEntity<Result<Boolean>> saveFavUserPost(@PathVariable("userId") Long userId, @PathVariable("userPostId") Long userPostId,@PathVariable("favPostFolderId") Long favPostFolderId){
        return CommonResult.success(favUserPostService.saveFavUserPost(userId,userPostId,favPostFolderId));
    }

    @DeleteMapping("api/userPost/removeFavUserPosts/{userId}")
    public ResponseEntity<Result<Boolean>> removeFavUserPosts(@PathVariable("userId") Long userId,@RequestBody List<Long> userPostIdList){
        return CommonResult.success(favUserPostService.removeFavUserPosts(userId,userPostIdList));
    }

    //****************SUBTASK******************
    @PostMapping("/api/subtask/saveSubtasksByPlanningId/{planningId}")
    public ResponseEntity<Result<Boolean>> saveSubtasksByPlanningId(@PathVariable("planningId") Long planningId,@RequestBody List<SubtaskParamDTO> subtaskParamDTOList){
        return CommonResult.success(subtaskService.saveSubtasksByPlanningId(planningId,subtaskParamDTOList));
    }

    @PutMapping("/api/subtask/updateSubtasksByPlanningId/{planningId}")
    public ResponseEntity<Result<Boolean>> updateSubtasksByPlanningId(@PathVariable("planningId") Long planningId,@RequestBody List<SubtaskParamDTO> subtaskParamDTOList){
        return CommonResult.success(subtaskService.updateSubtasksByPlanningId(planningId,subtaskParamDTOList));
    }

    @GetMapping("/api/subtask/countFinishSubtaskCount/{planningId}")
    public ResponseEntity<Result<Long>> countFinishSubtaskCount(@PathVariable("planningId") Long planningId){
        return CommonResult.success(subtaskService.countFinishSubtaskCount(planningId));
    }

    @DeleteMapping("/api/subtask/removeSubtasksByPlanningIdList")
    public ResponseEntity<Result<Boolean>> removeSubtasksByPlanningIdList(@RequestBody List<Long> planningIdList){
        return CommonResult.success(subtaskService.removeSubtasksByPlanningIdList(planningIdList));
    }

    @PutMapping("/api/subtask/bingo/{userPostId}/{subtaskId}")
    public ResponseEntity<Result<Boolean>> bingo(@PathVariable("userPostId") Long userPostId,@PathVariable("subtaskId") Long subtaskId){
        return CommonResult.success(subtaskService.bingo(userPostId,subtaskId));
    }

    @PutMapping("/api/subtask/cancelBingo/{userPostId}/{subtaskId}")
    public ResponseEntity<Result<Boolean>> cancelBingo(@PathVariable("userPostId") Long userPostId,@PathVariable("subtaskId") Long subtaskId){
        return CommonResult.success(subtaskService.cancelBingo(userPostId,subtaskId));
    }

    @PutMapping("/api/subtask/recoveryDeletedSubtasks")
    public ResponseEntity<Result<Boolean>> recoveryDeletedSubtasks(@RequestBody List<Long> planningIdList){
        return CommonResult.success(subtaskService.recoveryDeletedSubtasks(planningIdList));
    }

    @DeleteMapping("/api/subtask/removeDeletedSubtasks")
    public ResponseEntity<Result<Boolean>> removeDeletedSubtasks(@RequestBody List<Long> planningIdList){
        return CommonResult.success(subtaskService.removeDeletedSubtasks(planningIdList));
    }

    @GetMapping("/api/subtask/listSubtasksByPlanningId/{planningId}")
    public ResponseEntity<Result<List<SubtaskDTO>>> listSubtasksByPlanningId(@PathVariable("planningId") Long planningId){
        return CommonResult.success(subtaskService.listSubtasksByPlanningId(planningId));
    }

    @GetMapping("/api/category/getCategoryById/{categoryId}")
    public ResponseEntity<Result<Category>> getCategoryById(@PathVariable("categoryId") Long categoryId){
        return CommonResult.success(categoryService.getById(categoryId));
    }
}
