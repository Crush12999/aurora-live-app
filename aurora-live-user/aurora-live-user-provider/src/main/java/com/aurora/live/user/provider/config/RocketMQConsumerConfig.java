package com.aurora.live.user.provider.config;

import com.alibaba.fastjson.JSON;
import com.aurora.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.aurora.live.user.constants.CacheAsyncDeleteCode;
import com.aurora.live.user.constants.UserProviderTopicNames;
import com.aurora.live.user.model.dto.UserCacheAsyncDeleteDTO;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RocketMQ 的消费者 bean 配置类
 *
 * @author halo
 * @since 2024/3/26 23:42
 */
@Configuration
public class RocketMQConsumerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQConsumerProperties consumerProperties;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Override
    public void afterPropertiesSet() {
        initConsumer();
    }

    public void initConsumer() {
        try {
            // 初始化我们的RocketMQ消费者
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setVipChannelEnabled(false);
            defaultMQPushConsumer.setNamesrvAddr(consumerProperties.getNameSrv());
            defaultMQPushConsumer.setConsumerGroup(consumerProperties.getGroupName() + "_" + RocketMQConsumerConfig.class.getSimpleName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC, "");
            defaultMQPushConsumer.setMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                String msgStr = new String(msgList.get(0).getBody());
                UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = JSON.parseObject(msgStr, UserCacheAsyncDeleteDTO.class);
                if (CacheAsyncDeleteCode.USER_INFO_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    redisTemplate.delete(userProviderCacheKeyBuilder.buildUserInfoKey(userId));
                    LOGGER.info("延迟删除用户信息缓存，userId is {}", userId);
                } else if (CacheAsyncDeleteCode.USER_TAG_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    redisTemplate.delete(userProviderCacheKeyBuilder.buildTagKey(userId));
                    LOGGER.info("延迟删除用户标签缓存，userId is {}", userId);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            defaultMQPushConsumer.start();
            LOGGER.info("mq 消费者启动成功, nameSrv is {}", consumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }

}
