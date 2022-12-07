package com.wjs.examfrog.gateway.service;

import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.entity.User;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("ef-server-user")
public interface UserService {
    @GetMapping("/api/user/getUserByOpenId/{openId}")
    ResponseEntity<Result<User>> getUserByOpenId(@PathVariable("openId") String openId);
}
