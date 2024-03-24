package com.aurora.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 其他服务提供者的 Redis key
 *
 * @author halo
 * @since 2024/3/24 15:23
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class OtherProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static final String USER_INFO_KEY = "userInfo";

    public String buildUserInfoKey(Long userId) {
        return super.getPrefix() + USER_INFO_KEY + super.getSplitItem() + userId;
    }

}
