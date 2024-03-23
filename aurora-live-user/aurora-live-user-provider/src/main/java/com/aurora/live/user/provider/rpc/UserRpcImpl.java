package com.aurora.live.user.provider.rpc;

import com.aurora.live.user.interfaces.IUserRpc;
import com.aurora.live.user.model.dto.UserDTO;
import com.aurora.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户中台 rpc 实现
 *
 * @author halo
 * @since 2024/3/23 16:25
 */
@DubboService
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;

    /**
     * 根据 userId 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    @Override
    public UserDTO selectOneByUserId(Long userId) {
        return userService.selectOneByUserId(userId);
    }

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }
}
