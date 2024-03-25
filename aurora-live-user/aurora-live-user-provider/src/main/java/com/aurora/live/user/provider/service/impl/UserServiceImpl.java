package com.aurora.live.user.provider.service.impl;

import com.aurora.live.common.interfaces.ConvertBeanUtils;
import com.aurora.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.aurora.live.user.model.dto.UserDTO;
import com.aurora.live.user.provider.dao.entity.UserDO;
import com.aurora.live.user.provider.dao.mapper.UserMapper;
import com.aurora.live.user.provider.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * UserServiceImpl
 *
 * @author halo
 * @since 2024/3/23 22:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO>
        implements IUserService {

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Override
    public UserDTO selectOneByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        String redisKey = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        UserDTO userDTO = redisTemplate.opsForValue().get(redisKey);
        if (Objects.nonNull(userDTO)) {
            return userDTO;
        }
        userDTO = ConvertBeanUtils.convert(this.getById(userId), UserDTO.class);
        if (Objects.nonNull(userDTO)) {
            redisTemplate.opsForValue().set(redisKey, userDTO, 30, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     */
    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (Objects.isNull(userDTO) || Objects.isNull(userDTO.getUserId())) {
            return false;
        }
        return this.updateById(ConvertBeanUtils.convert(userDTO, UserDO.class));
    }

    /**
     * 新增一个用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @Override
    public boolean insertUser(UserDTO userDTO) {
        if (Objects.isNull(userDTO) || Objects.isNull(userDTO.getUserId())) {
            return false;
        }
        return baseMapper.insert(ConvertBeanUtils.convert(userDTO, UserDO.class)) > 0;
    }

    /**
     * 通过用户 ID 批量查询用户信息
     * 场景：进入直播间时批量查询直播间内发了弹幕的一些人的基础信息，比如头像时会使用
     *
     * @param userIds 用户 ID 列表
     * @return 用户信息
     */
    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Maps.newHashMap();
        }
        userIds = userIds.stream().filter(id -> id > 10000).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return Maps.newHashMap();
        }
        // redis，通过 multi get 批量获取
        List<String> keyList = new ArrayList<>();
        userIds.forEach(userId -> {
            keyList.add(userProviderCacheKeyBuilder.buildUserInfoKey(userId));
        });
        List<UserDTO> userDTOList = Optional.ofNullable(redisTemplate.opsForValue().multiGet(keyList)).orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userDTOList) && userDTOList.size() == userIds.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, e -> e));
        }

        // 在缓存里的 id
        List<Long> userIdInCacheList = userDTOList.stream()
                .map(UserDTO::getUserId)
                .collect(Collectors.toList());
        // 不在缓存里的 id
        List<Long> userIdNotInCacheList = userIds.stream()
                .filter(e -> !userIdInCacheList.contains(e))
                .collect(Collectors.toList());


        // 使用多线程查询归并，防止直接通过 ShardingJDBC 查询，全部在数据库层面做 union all 性能不好
        Map<Long, List<Long>> userIdMap = userIdNotInCacheList.stream().collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> dbQueryResult = new CopyOnWriteArrayList<>();
        // 并行流执行
        userIdMap.values().parallelStream().forEach(queryUserIdList -> {
            dbQueryResult.addAll(ConvertBeanUtils.convertList(baseMapper.selectBatchIds(queryUserIdList), UserDTO.class));
        });

        // 将查询出来的结果缓存到 Redis
        if (!CollectionUtils.isEmpty(dbQueryResult)) {
            Map<String, UserDTO> saveCacheMap = dbQueryResult.stream()
                    .collect(
                            Collectors.toMap(
                                    userDTO -> userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId()),
                                    e -> e
                            )
                    );
            redisTemplate.opsForValue().multiSet(saveCacheMap);
            // 使用管道批量传输过期时间命令，减少网络 IO 开销
            redisTemplate.executePipelined(new SessionCallback<>() {
                @Override
                public <K, V> Object execute(@Nonnull RedisOperations<K, V> operations) throws DataAccessException {
                    for (String redisKey : saveCacheMap.keySet()) {
                        operations.expire((K) redisKey, createRandomExpireTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            userDTOList.addAll(dbQueryResult);
        }
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, e -> e));
    }

    /**
     * 随机时间 + 业务预期失效时间，防止同一时间内缓存大量失效进而导致系统 DB 压力大
     * 过期时间 = base 时间 + 随机时间
     *
     * @return 缓存过期时间，单位: 秒
     */
    private int createRandomExpireTime() {
        int randomNumSecond = ThreadLocalRandom.current().nextInt(1000);
        return randomNumSecond + 60 * 30;
    }
}
