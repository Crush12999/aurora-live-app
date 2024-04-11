package com.aurora.live.user.constants;

/**
 * 异步删除缓存代码枚举
 *
 * @author halo
 * @since 2024/4/11 23:07
 */
public enum CacheAsyncDeleteCode {

    /**
     * 用户基础信息删除
     */
    USER_INFO_DELETE(0, "用户基础信息删除"),

    /**
     * 用户标签删除
     */
    USER_TAG_DELETE(1, "用户标签删除"),
    ;

    private final int code;

    private final String desc;

    CacheAsyncDeleteCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
