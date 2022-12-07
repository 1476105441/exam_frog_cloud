package com.wjs.examfrog.other.service;

import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.entity.RoleAdminRelation;
import com.wjs.examfrog.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ef-server-user")
public interface ApiService {

    @GetMapping("/api/user/getUserById/{userId}")
    ResponseEntity<Result<User>> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/user/checkOperator/{userId}")
    ResponseEntity<Result<Boolean>> checkOperator(@PathVariable("userId") Long userId);

    @GetMapping("/api/roleAdminRelation/getOne/{userId}")
    ResponseEntity<Result<RoleAdminRelation>> getOne(@PathVariable("userId") Long userId);
}
