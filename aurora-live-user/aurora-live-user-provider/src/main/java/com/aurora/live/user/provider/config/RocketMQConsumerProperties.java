package com.aurora.live.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消费者的配置信息
 *
 * @author halo
 * @since 2024/3/26 23:36
 */
@ConfigurationProperties(prefix = "aurora.rmq.consumer")
@Configuration
public class RocketMQConsumerProperties {

    /**
     * RocketMQ 的 nameSever 地址
     */
    private String nameSrv;

    /**
     * 分组名称
     */
    private String groupName;

    public String getNameSrv() {
        return nameSrv;
    }

    public void setNameSrv(String nameSrv) {
        this.nameSrv = nameSrv;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "RocketMQConsumerProperties{" +
                "nameSrv='" + nameSrv + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }

}
