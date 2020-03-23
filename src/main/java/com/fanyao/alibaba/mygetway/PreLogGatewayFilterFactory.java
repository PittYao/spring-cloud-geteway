package com.fanyao.alibaba.mygetway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author: bugProvider
 * @date: 2020/3/20 14:34
 * @description: 自定义过滤器工厂
 * - 类名必须以 GatewayFilterFactory 结尾,否则启动报错
 * - 配置过滤器时 以PreLog 为名称前缀在 yml filters进行配置
 */
@Slf4j
@Component
public class PreLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {

         GatewayFilter gatewayFilter =  ((exchange, chain) -> {
            log.info("请求进入自定义过滤器 获取配置文件的 key = {} | value = {}", config.getName(), config.getValue());
            //  获取请求 可以对请求进行修改
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getURI().toString();
            String auth = request.getHeaders().getFirst("Auth");
            log.info("获取请求中的信息 {} | {}", url, auth);

            // 修改request中的header的值
            ServerHttpRequest modifyRequest = exchange.getRequest().mutate()
                    .header("Auth","6")
                    .build();

            ServerWebExchange serverWebExchange = exchange.mutate().request(modifyRequest).build();

            return chain.filter(serverWebExchange);
        });
         // 设置执行order大小
        return new OrderedGatewayFilter(gatewayFilter, 2);
    }
}
