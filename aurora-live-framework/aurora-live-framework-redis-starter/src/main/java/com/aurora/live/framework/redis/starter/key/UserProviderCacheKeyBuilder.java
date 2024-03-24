package com.aurora.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 用户中台专属的 keyBuilder
 *
 * @author halo
 * @since 2024/3/24 14:23
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class UserProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String USER_INFO_KEY = "userInfo";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }

}
