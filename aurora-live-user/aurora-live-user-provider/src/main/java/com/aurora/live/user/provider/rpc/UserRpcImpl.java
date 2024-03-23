package com.aurora.live.user.provider.rpc;

import com.aurora.live.user.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户中台 rpc 实现
 *
 * @author halo
 * @since 2024/3/23 16:25
 */
@DubboService
public class UserRpcImpl implements IUserRpc {

    @Override
    public String test() {
        System.out.println("hello dubbo");
        return "success";
    }
}
