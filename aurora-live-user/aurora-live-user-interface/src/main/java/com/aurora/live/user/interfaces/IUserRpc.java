package com.aurora.live.user.interfaces;

import com.aurora.live.user.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

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

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * 新增一个用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean insertUser(UserDTO userDTO);

    /**
     * 通过用户 ID 批量查询用户信息
     *
     * @param userIds 用户 ID 列表
     * @return 用户信息
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIds);

}
