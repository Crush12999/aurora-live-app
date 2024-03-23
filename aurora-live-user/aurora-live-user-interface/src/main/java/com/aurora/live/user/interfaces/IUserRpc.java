package com.aurora.live.user.interfaces;

import com.aurora.live.user.model.dto.UserDTO;

/**
 * user rpc
 *
 * @author halo
 * @since 2024/3/23 16:19
 */
public interface IUserRpc {

    /**
     * 根据 userId 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     */
    UserDTO selectOneByUserId(Long userId);

}
