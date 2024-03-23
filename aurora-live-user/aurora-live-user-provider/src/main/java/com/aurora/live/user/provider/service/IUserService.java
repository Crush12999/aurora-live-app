package com.aurora.live.user.provider.service;

import com.aurora.live.user.model.dto.UserDTO;
import com.aurora.live.user.provider.dao.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户 Service
 *
 * @author halo
 * @since 2024/3/23 22:05
 */
public interface IUserService extends IService<UserDO> {

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
}
