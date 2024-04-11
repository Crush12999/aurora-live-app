package com.aurora.live.user.constants;

/**
 * UserProvider Topic 名称常量
 *
 * @author halo
 * @since 2024/4/11 23:13
 */
public interface UserProviderTopicNames {

    /**
     * 专门处理和用户信息相关的缓存延迟删除操作
     */
    String CACHE_ASYNC_DELETE_TOPIC = "UserCacheAsyncDelete";
}
