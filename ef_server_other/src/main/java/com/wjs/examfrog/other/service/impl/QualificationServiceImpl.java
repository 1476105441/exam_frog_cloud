package com.wjs.examfrog.other.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.Qualification;
import com.wjs.examfrog.other.mapper.QualificationMapper;
import com.wjs.examfrog.other.service.QualificationService;
import org.springframework.stereotype.Service;

@Service
public class QualificationServiceImpl extends ServiceImpl<QualificationMapper, Qualification> implements QualificationService {
}
