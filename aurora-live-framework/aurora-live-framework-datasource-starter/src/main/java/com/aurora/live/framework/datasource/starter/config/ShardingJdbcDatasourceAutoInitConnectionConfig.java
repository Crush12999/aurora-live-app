package com.aurora.live.framework.datasource.starter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * ShardingJDBC 数据源自动初始化, 用于初始化数据库连接池的时候去提前建立连接
 * 后续考虑如何将这个配置类做成一个参数控制
 *
 * @author halo
 * @since 2024/3/24 01:52
 */
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info("dataSource: {}", dataSource);
            // 手动触发下连接池的连接创建
            Connection connection = dataSource.getConnection();
        };
    }
}
