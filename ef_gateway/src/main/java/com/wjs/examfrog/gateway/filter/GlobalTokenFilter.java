package com.wjs.examfrog.gateway.filter;

import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.gateway.service.UserService;
import com.wjs.examfrog.gateway.util.JwtTokenUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

@Component
public class GlobalTokenFilter implements GlobalFilter, Ordered {
    @Resource
    UserService userService;

    @Resource
    JwtTokenUtil util;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        List<String> tokens = request.getQueryParams().get("token");

        if (!(uri != null && !"".equals(uri) && uri.contains("/account")) && tokens == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);

            return response.setComplete();
        }
        if (tokens != null) {
            String token = tokens.get(0);
            //还需要检验token是否合法
            String openId = util.getUserNameFromToken(token);
            if (openId == null) {
                response.setStatusCode(HttpStatus.FORBIDDEN);

                return response.setComplete();
            }
            User user = userService.getUserByOpenId(openId).getBody().getData();
            if (user == null || !util.validateToken(token, user)) {
                response.setStatusCode(HttpStatus.FORBIDDEN);

                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
