package com.aurora.live.framework.redis.starter.config;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Objects;

/**
 * 自定义 Redis 序列化器
 *
 * @author halo
 * @since 2024/3/24 14:09
 */
public class IGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

    public IGenericJackson2JsonRedisSerializer() {
        super(MapperFactory.newInstance());
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {

        if (Objects.nonNull(source) && ((source instanceof String) || (source instanceof Character))) {
            return source.toString().getBytes();
        }
        return super.serialize(source);
    }
}
