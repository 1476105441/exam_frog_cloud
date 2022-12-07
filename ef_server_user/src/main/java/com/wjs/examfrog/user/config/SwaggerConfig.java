package com.wjs.examfrog.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;


/**
 * Swagger 配置
 */
@Profile({"dev", "test"})
@EnableSwagger2WebMvc
@Configuration
public class SwaggerConfig {
    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        // 指定扫描包路径
        return new Docket(DocumentationType.SWAGGER_2) // 指定生成的文档的类型是Swagger2
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wjs.examfrog.user.controller"))
                .paths(PathSelectors.any()).build();
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("用户模块")
                // 描述
                .description("用户模块的开发文档")
                // 版本
                .version("版本号: 1.0.1")
                .build();
    }

}
