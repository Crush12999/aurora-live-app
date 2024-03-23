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
}
