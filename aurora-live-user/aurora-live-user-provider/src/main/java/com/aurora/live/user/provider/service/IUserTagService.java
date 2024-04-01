package com.aurora.live.user.provider.service;

import com.aurora.live.user.constants.UserTagsEnum;

/**
 * 用户标签 service
 *
 * @author halo
 * @since 2024/4/1 23:29
 */
public interface IUserTagService {

    /**
     * 设置标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要设置的标签
     * @return 设置结果
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消标签
     *
     * @param userId       用户 id
     * @param userTagsEnum 要取消的用户标签
     * @return 取消结果
     */
    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 是否包含某个标签
     *
     * @param userId       用户 ID
     * @param userTagsEnum 用户标签
     * @return 是否包含
     */
    boolean containTag(Long userId, UserTagsEnum userTagsEnum);

}
