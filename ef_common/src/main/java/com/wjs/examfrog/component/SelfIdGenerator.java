package com.wjs.examfrog.component;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



public class SelfIdGenerator implements IdentifierGenerator {

    @Value("${hutool.idutil.workerId}")
    private Long workerId;
    @Value("${hutool.idutil.datacenterId}")
    private Long datacenterId;

    @Override
    public Long nextId(Object entity) {
        return IdUtil.getSnowflake(workerId, datacenterId).nextId();
    }

}
