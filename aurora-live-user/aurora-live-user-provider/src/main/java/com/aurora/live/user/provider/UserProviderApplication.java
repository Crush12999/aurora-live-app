package com.aurora.live.user.provider;

import com.aurora.live.user.constants.UserTagsEnum;
import com.aurora.live.user.provider.service.IUserTagService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户中台服务提供者
 *
 * @author halo
 * @since 2024/3/23 16:14
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication implements CommandLineRunner {

    @Resource
    private IUserTagService userTagService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Long userId = 1212L;
        System.out.println("(old) 当前用户是否拥有 is_rich 标签：" + userTagService.containTag(userId, UserTagsEnum.IS_RICH));
        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));
        System.out.println("(set) 当前用户是否拥有 is_rich 标签：" + userTagService.containTag(userId, UserTagsEnum.IS_RICH));
        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
        System.out.println("(cancel) 当前用户是否拥有 is_rich 标签：" + userTagService.containTag(userId, UserTagsEnum.IS_RICH));

    }
}
