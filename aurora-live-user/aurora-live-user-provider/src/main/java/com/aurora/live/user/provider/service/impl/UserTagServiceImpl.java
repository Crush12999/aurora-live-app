package com.aurora.live.user.provider.service.impl;

import com.aurora.live.user.constants.UserTagFieldNameConstant;
import com.aurora.live.user.constants.UserTagsEnum;
import com.aurora.live.user.provider.dao.entity.UserTagDO;
import com.aurora.live.user.provider.dao.mapper.UserTagMapper;
import com.aurora.live.user.provider.service.IUserTagService;
import com.aurora.live.user.utils.TagInfoUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户标签 service impl
 *
 * @author halo
 * @since 2024/4/1 23:30
 */
@Service
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private UserTagMapper userTagMapper;

    /**
     * 设置标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要设置的标签
     * @return 设置结果
     */
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
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
        return userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
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
        UserTagDO userTagDO = userTagMapper.selectById(userId);
        if (Objects.isNull(userTagDO)) {
            return false;
        }
        String queryFieldName = userTagsEnum.getFieldName();
        if (UserTagFieldNameConstant.TAG_INFO_01.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo01(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstant.TAG_INFO_02.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo02(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstant.TAG_INFO_03.equals(queryFieldName)) {
            return TagInfoUtil.isContain(userTagDO.getTagInfo03(), userTagsEnum.getTag());
        }
        return false;
    }
}
