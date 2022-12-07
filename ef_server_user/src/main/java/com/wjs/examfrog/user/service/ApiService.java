package com.wjs.examfrog.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.PageParamDTO;
import com.wjs.examfrog.dto.SubtaskDTO;
import com.wjs.examfrog.dto.SubtaskParamDTO;
import com.wjs.examfrog.dto.UserPostParamDTO;
import com.wjs.examfrog.entity.Category;
import com.wjs.examfrog.entity.UserPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "ef-server-other")
public interface ApiService {
    @PostMapping(path = "/api/userPost/publish/{userId}")
    ResponseEntity<Result<Boolean>> publishUserPost(@PathVariable("userId") Long userId, @RequestBody UserPostParamDTO userPostParamDTO);

    @PostMapping(path = "/api/userPost/update/{userId}/{userPostId}")
    ResponseEntity<Result<Boolean>> updateUserPost(@PathVariable("userId") Long userId,@PathVariable("userPostId") Long userPostId,@RequestBody UserPostParamDTO userPostParamDTO);

    @DeleteMapping(path = "/api/userPost/{userId}")
    ResponseEntity<Result<Boolean>> removeUserPosts(@PathVariable("userId") Long userId, @RequestBody List<Long> userPostIdList);

    @GetMapping(path = "/api/userPost/listById/{userId}")
    ResponseEntity<Result<List<UserPost>>> listUserPost(@PathVariable("userId") Long userId);

    @GetMapping(path = "/api/userPost/getPostById/{userPostId}")
    ResponseEntity<Result<UserPost>> getPostById(@PathVariable("userPostId") Long userPostId);

    @PutMapping("/api/userPost/updateOneById")
    ResponseEntity<Result<Boolean>> updateOneById(@RequestBody UserPost userPost);

    @PutMapping("/api/userPost/increaseSuccessCount/{userPostId}")
    ResponseEntity<Result<Boolean>> increaseSuccessCount(@PathVariable("userPostId") Long userPostId);

    @PutMapping("/api/userPost/listFavUserPosts/{userId}")
    ResponseEntity<Result<Page>> listFavUserPosts(@PathVariable("userId") Long userId,@RequestBody PageParamDTO pageParamDTO);

    @GetMapping("/api/userPost/listPostByIds")
    ResponseEntity<Result<List<UserPost>>> listPostByIds(@RequestBody List<Long> ids);

    @GetMapping("/api/userPost/updateBatchPostById")
    ResponseEntity<Result<Boolean>> updateBatchPostById(@RequestBody List<UserPost> userPostList);


    //***********************************************************************
    //**********************************subtask*********************************
    @PostMapping("/api/subtask/saveSubtasksByPlanningId/{planningId}")
    ResponseEntity<Result<Boolean>> saveSubtasksByPlanningId(@PathVariable("planningId") Long planningId, @RequestBody List<SubtaskParamDTO> subtaskParamDTOList);

    @PutMapping("/api/subtask/updateSubtasksByPlanningId/{planningId}")
    ResponseEntity<Result<Boolean>> updateSubtasksByPlanningId(@PathVariable("planningId") Long planningId,@RequestBody List<SubtaskParamDTO> subtaskParamDTOList);

    @GetMapping("/api/subtask/countFinishSubtaskCount/{planningId}")
    ResponseEntity<Result<Long>> countFinishSubtaskCount(@PathVariable("planningId") Long planningId);

    @DeleteMapping("/api/subtask/removeSubtasksByPlanningIdList")
    ResponseEntity<Result<Boolean>> removeSubtasksByPlanningIdList(@RequestBody List<Long> planningIdList);

    @PutMapping("/api/subtask/bingo/{userPostId}/{subtaskId}")
    ResponseEntity<Result<Boolean>> bingo(@PathVariable("userPostId") Long userPostId,@PathVariable("subtaskId") Long subtaskId);

    @PutMapping("/api/subtask/cancelBingo/{userPostId}/{subtaskId}")
    ResponseEntity<Result<Boolean>> cancelBingo(@PathVariable("userPostId") Long userPostId,@PathVariable("subtaskId") Long subtaskId);

    @PutMapping("/api/subtask/recoveryDeletedSubtasks")
    ResponseEntity<Result<Boolean>> recoveryDeletedSubtasks(@RequestBody List<Long> planningIdList);

    @DeleteMapping("/api/subtask/removeDeletedSubtasks")
    ResponseEntity<Result<Boolean>> removeDeletedSubtasks(@RequestBody List<Long> planningIdList);

    @GetMapping("/api/subtask/listSubtasksByPlanningId/{planningId}")
    ResponseEntity<Result<List<SubtaskDTO>>> listSubtasksByPlanningId(@PathVariable("planningId") Long planningId);


    //*******************************************************************
    //******************************favUserPost*************************************

    @PutMapping("api/userPost/saveFavUserPost/{userId}/{userPostId}/{favPostFolderId}")
    ResponseEntity<Result<Boolean>> saveFavUserPost(@PathVariable("userId") Long userId, @PathVariable("userPostId") Long userPostId,@PathVariable("favPostFolderId") Long favPostFolderId);

    @DeleteMapping("api/userPost/removeFavUserPosts/{userId}")
    ResponseEntity<Result<Boolean>> removeFavUserPosts(@PathVariable("userId") Long userId,@RequestBody List<Long> userPostIdList);

    @GetMapping("/api/category/getCategoryById/{categoryId}")
    ResponseEntity<Result<Category>> getCategoryById(@PathVariable("categoryId") Long categoryId);
}
