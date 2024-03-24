package com.aurora.live.user.provider.service;

import com.aurora.live.user.model.dto.UserDTO;
import com.aurora.live.user.provider.dao.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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
