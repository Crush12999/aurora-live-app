package com.aurora.live.user.provider.service.impl;

import com.aurora.live.common.interfaces.ConvertBeanUtils;
import com.aurora.live.user.model.dto.UserDTO;
import com.aurora.live.user.provider.dao.entity.UserDO;
import com.aurora.live.user.provider.dao.mapper.UserMapper;
import com.aurora.live.user.provider.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * UserServiceImpl
 *
 * @author halo
 * @since 2024/3/23 22:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO>
        implements IUserService {

    @Override
    public UserDTO selectOneByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        return ConvertBeanUtils.convert(this.getById(userId), UserDTO.class);
    }

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     */
    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (Objects.isNull(userDTO) || Objects.isNull(userDTO.getUserId())) {
            return false;
        }
        return this.updateById(ConvertBeanUtils.convert(userDTO, UserDO.class));
    }

    /**
     * 新增一个用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    @Override
    public boolean insertUser(UserDTO userDTO) {
        if (Objects.isNull(userDTO) || Objects.isNull(userDTO.getUserId())) {
            return false;
        }
        return baseMapper.insert(ConvertBeanUtils.convert(userDTO, UserDO.class)) > 0;
    }
}
