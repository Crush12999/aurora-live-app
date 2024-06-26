create database aurora_live_common character set utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE `t_id_generate_config`
(
    `id`             int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
    `next_threshold` bigint                                                        DEFAULT NULL COMMENT '当前id所在阶段的阈值',
    `init_num`       bigint                                                        DEFAULT NULL COMMENT '初始化值',
    `current_start`  bigint                                                        DEFAULT NULL COMMENT '当前id所在阶段的开始值',
    `step`           int                                                           DEFAULT NULL COMMENT 'id递增区间',
    `is_seq`         tinyint                                                       DEFAULT NULL COMMENT '是否有序（0无序，1有序）',
    `id_prefix`      varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '业务前缀码，如果没有则返回时不携带',
    `version`        int NOT NULL                                                  DEFAULT '0' COMMENT '乐观锁版本号',
    `create_time`    datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

INSERT INTO `t_id_generate_config` (`id`, `remark`, `next_threshold`, `init_num`, `current_start`, `step`, `is_seq`,
                                    `id_prefix`, `version`, `create_time`, `update_time`)
VALUES (1, '用户 id 生成策略', 10050, 10000, 10000, 50, 0, 'user_id', 0, '2024-03-29 12:38:21', '2024-03-29 23:31:45');