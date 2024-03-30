package com.aurora.live.id.generate.provider.rpc;

import com.aurora.live.id.generate.interfaces.IdGenerateRpc;
import com.aurora.live.id.generate.provider.service.IdGenerateService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 分布式 id 生成服务 rpc 对外接口实现
 *
 * @author halo
 * @since 2024/3/30 00:13
 */
@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {

    @Resource
    private IdGenerateService idGenerateService;

    /**
     * 生成有序 id
     *
     * @param code 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    @Override
    public Long getSeqId(Integer code) {
        return idGenerateService.getSeqId(code);
    }

    /**
     * 生成无序 id
     *
     * @param code 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    @Override
    public Long getUnSeqId(Integer code) {
        return idGenerateService.getUnSeqId(code);
    }
}
