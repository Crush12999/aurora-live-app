package com.aurora.live.user.utils;

import java.util.Objects;

/**
 * 用户标签工具类
 *
 * @author halo
 * @since 2024/4/1 23:49
 */
public class TagInfoUtil {

    /**
     * 判断是否存在某个标签
     *
     * @param tagInfo  用户当前的标签值
     * @param matchTag 被查询是否匹配的标签值
     * @return 是否存在
     */
    public static boolean isContain(Long tagInfo, Long matchTag) {
        // 需要根据标签枚举中的 fieldName 来识别需要匹配 MySQL 表中哪个字段的标签值
        return Objects.nonNull(tagInfo) && Objects.nonNull(matchTag) && matchTag > 0 && (tagInfo & matchTag) == matchTag;
    }

}
