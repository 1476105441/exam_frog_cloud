package com.wjs.examfrog.user.config;

import com.wjs.examfrog.component.SelfIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public SelfIdGenerator selfIdGenerator(){
        return new SelfIdGenerator();
    }
}
