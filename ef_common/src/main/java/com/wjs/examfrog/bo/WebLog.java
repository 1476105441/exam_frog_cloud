package com.wjs.examfrog.bo;

import lombok.Data;


/**
 * Controller层的日志封装类
 */
@Data
public class WebLog {

    /**
     * 操作描述
     */
    private String description;

    /**
     * URL
     */
    private String url;

    /**
     * Header
     */
    private String token;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private Object requestParameter;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 操作用户
     */
    private String username;


    /**
     * 方法参数
     */
    private Object parameter;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Long spendTime;

    /**
     * 请求返回的结果
     */
    private Object result;

}
