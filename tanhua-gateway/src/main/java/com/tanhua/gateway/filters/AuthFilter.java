package com.tanhua.gateway.filters;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/23 14:10
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${gateway.excludedUrls}")
    private List<String> excludeUrls;

    // 核心业务过滤
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        System.out.println("请求路径： " + path);
        excludeUrls.forEach(System.out:: println);
        // 登录直接放行
        if(excludeUrls.contains(path)){
            chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 清理token
        if(!StrUtil.isNotEmpty(token)){
            token = token.replace("Bearer ", "");
        }
        // 鉴权
        boolean ret = JwtUtils.verifyToken(token);
        if(! ret){
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", 401);
            responseData.put("errMessage", "用户未登录");
            return responseError(exchange.getResponse(), responseData);
        }
        // 鉴权成功，放行
        return chain.filter(exchange);
    }

    // 设置过滤器顺序
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    //响应错误数据
    private Mono<Void> responseError(ServerHttpResponse response, Map<String, Object> responseData){
        // 将信息转换为 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = new byte[0];
        try {
            data = objectMapper.writeValueAsBytes(responseData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 输出错误信息到页面
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
