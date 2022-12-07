package com.wjs.examfrog.other.service.impl;

import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.entity.RoleAdminRelation;
import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.other.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {
    @Override
    public ResponseEntity<Result<User>> getUserById(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<Result<Boolean>> checkOperator(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<Result<RoleAdminRelation>> getOne(Long userId) {
        return null;
    }
}
