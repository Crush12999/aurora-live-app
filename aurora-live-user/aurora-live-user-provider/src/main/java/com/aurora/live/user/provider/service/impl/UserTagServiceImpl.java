package com.aurora.live.user.provider.service.impl;

import com.aurora.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.aurora.live.user.constants.UserTagFieldNameConstant;
import com.aurora.live.user.constants.UserTagsEnum;
import com.aurora.live.user.provider.dao.entity.UserTagDO;
import com.aurora.live.user.provider.dao.mapper.UserTagMapper;
import com.aurora.live.user.provider.service.IUserTagService;
import com.aurora.live.user.utils.TagInfoUtil;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 用户标签 service impl
 *
 * @author halo
 * @since 2024/4/1 23:30
 */
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private UserTagMapper userTagMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    /**
     * 设置标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要设置的标签
     * @return 设置结果
     */
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        // 尝试 update，如果是 true 说明更新成功
        // 失败：1.已经设置过了标签；2.数据库没有初始化记录
        boolean updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        if (updateStatus) {
            return true;
        }

        // 原子指令防止并发问题
        String setNxKey = cacheKeyBuilder.buildTagLockKey(userId);
        String sexNxRes = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                return (String) connection.execute(
                        "set",
                        keySerializer.serialize(setNxKey),
                        valueSerializer.serialize("-1"),
                        "nx".getBytes(StandardCharsets.UTF_8),
                        "ex".getBytes(StandardCharsets.UTF_8),
                        "3".getBytes(StandardCharsets.UTF_8)
                );
            }
        });
        if (!"OK".equals(sexNxRes)) {
            return false;
        }

        UserTagDO userTagDO = userTagMapper.selectById(userId);
        if (Objects.nonNull(userTagDO)) {
            // 设置过了
            return false;
        }
        userTagDO = new UserTagDO();
        userTagDO.setUserId(userId);
        userTagMapper.insert(userTagDO);
        updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        redisTemplate.delete(setNxKey);
        return updateStatus;
    }

    /**
     * 取消标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要取消的用户标签
     * @return 取消结果
     */
    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
    }

    /**
     * 是否包含某个标签
     *
     * @param userId       用户 ID
     * @param userTagsEnum 用户标签
     * @return 是否包含
     */
    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDO userTagDO = userTagMapper.selectById(userId);
        if (Objects.isNull(userTagDO)) {
            return false;
        }
        String queryFieldName = userTagsEnum.getFieldName();
        if (UserTagFieldNameConstant.TAG_INFO_01.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo01(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstant.TAG_INFO_02.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo02(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstant.TAG_INFO_03.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }
}
