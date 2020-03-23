package com.fanyao.alibaba.mygetway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
public class MyGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyGateWayApplication.class, args);
    }

    // TODO 整合 sentinel

    // 全局过滤器  按照Order的值依次过滤 值越小 越先执行
    @Bean
    @Order(2)
    public GlobalFilter myGlobalFilter() {
        return (exchange, chain) -> {
            log.info("执行全局过滤器开始");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("执行全局过滤器完成");
            }));
        };
    }

}
