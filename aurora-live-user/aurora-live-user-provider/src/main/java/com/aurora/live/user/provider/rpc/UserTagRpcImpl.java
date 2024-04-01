package com.aurora.live.user.provider.rpc;

import com.aurora.live.user.constants.UserTagsEnum;
import com.aurora.live.user.interfaces.IUserTagRpc;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户标签 RPC 服务实现
 *
 * @author halo
 * @since 2024/4/1 23:28
 */
@DubboService
public class UserTagRpcImpl implements IUserTagRpc {

    /**
     * 设置标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要设置的标签
     * @return 设置结果
     */
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
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
        return false;
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
        return false;
    }
}
