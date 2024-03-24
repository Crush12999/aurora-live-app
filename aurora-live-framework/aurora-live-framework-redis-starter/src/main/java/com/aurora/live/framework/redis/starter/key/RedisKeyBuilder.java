package com.aurora.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * 统一的 Key 管理类, 每个 Redis 的 key 都能有统一的前缀进行管理和匹配
 *
 * @author halo
 * @since 2024/3/24 14:18
 */
public class RedisKeyBuilder {

    @Value("${spring.application.name}")
    private String applicationName;
    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }

}
