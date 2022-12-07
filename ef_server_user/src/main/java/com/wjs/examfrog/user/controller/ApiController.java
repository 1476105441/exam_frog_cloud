package com.wjs.examfrog.user.controller;

import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.entity.RoleAdminRelation;
import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.user.service.RoleAdminRelationService;
import com.wjs.examfrog.user.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ApiController {

    @Resource
    private UserService userService;

    @Resource
    private RoleAdminRelationService roleAdminRelationService;

    @GetMapping("/api/user/getUserById/{userId}")
    public ResponseEntity<Result<User>> getUserById(@PathVariable("userId") Long userId) {
        return CommonResult.success(userService.getById(userId));
    }

    @GetMapping("/api/user/checkOperator/{userId}")
    public ResponseEntity<Result<Boolean>> checkOperator(@PathVariable("userId") Long userId) {
        return CommonResult.success(userService.checkOperator(userId));
    }

    @GetMapping("/api/user/getUserByOpenId/{openId}")
    public ResponseEntity<Result<User>> getUserByOpenId(@PathVariable("openId") String openId){
        return CommonResult.success(userService.getUserByOpenId(openId));
    }

    //***********************************************************************
    //************************roleAdminRelation******************************
    @GetMapping("/api/roleAdminRelation/getOne/{userId}")
    ResponseEntity<Result<RoleAdminRelation>> getOne(@PathVariable("userId") Long userId){
        return CommonResult.success(roleAdminRelationService.getOne(userId));
    }
}
