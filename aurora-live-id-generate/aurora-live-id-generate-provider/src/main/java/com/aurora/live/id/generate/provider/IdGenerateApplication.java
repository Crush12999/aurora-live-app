package com.aurora.live.id.generate.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 分布式 id 生成器服务提供者
 *
 * @author halo
 * @since 2024/3/29 23:53
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
