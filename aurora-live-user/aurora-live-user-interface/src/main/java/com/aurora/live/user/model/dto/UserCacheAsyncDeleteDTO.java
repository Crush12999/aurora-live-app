package com.aurora.live.user.model.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户缓存异步删除 dto
 *
 * @author halo
 * @since 2024/4/11 23:17
 */
public class UserCacheAsyncDeleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8288042831126328316L;

    private Integer code;

    private String json;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "UserCacheAsyncDeleteDTO{" +
                "code=" + code +
                ", json='" + json + '\'' +
                '}';
    }
}
