package com.wjs.examfrog.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.Admin;
import com.wjs.examfrog.user.mapper.AdminMapper;
import com.wjs.examfrog.user.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
