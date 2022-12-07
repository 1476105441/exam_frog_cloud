package com.wjs.examfrog.user.controller;


import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.dto.AdminDTO;
import com.wjs.examfrog.dto.LoginParamDTO;
import com.wjs.examfrog.user.service.UserService;
import com.wjs.examfrog.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@Api(tags = "Account的API")
@RequestMapping("/account")
@RestController
public class AccountController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "wx端注册", notes = "用于微信端注册")
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<Result<UserLoginVO>> register(@Valid @RequestBody LoginParamDTO loginParamDTO) {
        UserLoginVO userLoginVO = userService.register(loginParamDTO);
        return CommonResult.success(userLoginVO);
    }

    @ApiOperation(value = "wx端登录", notes = "用于微信端登录")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Result<UserLoginVO>> login(@ApiParam(value = "openId", required = true) @RequestParam String openId) {
        UserLoginVO userLoginVO = userService.login(openId);
        return CommonResult.success(userLoginVO);
    }

    @ApiOperation(value = "后台管理———登录", notes = "用于微信端登录")
    @RequestMapping(path = "/admin-login", method = RequestMethod.POST)
    public ResponseEntity<Result<UserLoginVO>> adminLogin(@RequestBody AdminDTO adminDTO) {
        UserLoginVO userLoginVO = userService.adminLogin(adminDTO);
        return CommonResult.success(userLoginVO);
    }
}
