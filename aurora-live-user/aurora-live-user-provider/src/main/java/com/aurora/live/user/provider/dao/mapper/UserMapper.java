package com.aurora.live.user.provider.dao.mapper;

import com.aurora.live.user.provider.dao.entity.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 mapper 层
 *
 * @author halo
 * @since 2024/3/23 22:04
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
