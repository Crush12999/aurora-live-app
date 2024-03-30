package com.aurora.live.id.generate.interfaces;

/**
 * 分布式 id 生成服务 rpc 对外接口
 *
 * @author halo
 * @since 2024/3/30 00:05
 */
public interface IdGenerateRpc {

    /**
     * 生成有序 id
     *
     * @param code 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    Long getSeqId(Integer code);

    /**
     * 生成无序 id
     *
     * @param code 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    Long getUnSeqId(Integer code);

}
