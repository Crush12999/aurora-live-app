package com.aurora.live.user.constants;

/**
 * 用户标签枚举
 *
 * @author halo
 * @since 2024/4/1 23:23
 */
public enum UserTagsEnum {

    /**
     * 是否是有钱用户
     */
    IS_RICH(1L, "有钱人", "tag_info_01"),

    /**
     * 是否是VIP用户
     */
    IS_VIP(1L << 1, "是否是VIP用户", "tag_info_01"),

    /**
     * 是否是老用户
     */
    IS_OLD_USER(1L << 2, "是否是老用户", "tag_info_01"),

    ;

    /**
     * tag 值
     */
    private final long tag;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 字段名称
     */
    private final String fieldName;

    UserTagsEnum(long tag, String desc, String fieldName) {
        this.tag = tag;
        this.desc = desc;
        this.fieldName = fieldName;
    }

    public long getTag() {
        return tag;
    }

    public String getDesc() {
        return desc;
    }

    public String getFieldName() {
        return fieldName;
    }

}
