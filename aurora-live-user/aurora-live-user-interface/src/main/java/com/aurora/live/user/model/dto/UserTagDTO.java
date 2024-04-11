package com.aurora.live.user.model.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户标签 dto
 *
 * @author halo
 * @since 2024/4/11 23:20
 */
public class UserTagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5165406916451527901L;

    private Long userId;

    private Long tagInfo01;

    private Long tagInfo02;

    private Long tagInfo03;

    private Date createTime;

    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTagInfo01() {
        return tagInfo01;
    }

    public void setTagInfo01(Long tagInfo01) {
        this.tagInfo01 = tagInfo01;
    }

    public Long getTagInfo02() {
        return tagInfo02;
    }

    public void setTagInfo02(Long tagInfo02) {
        this.tagInfo02 = tagInfo02;
    }

    public Long getTagInfo03() {
        return tagInfo03;
    }

    public void setTagInfo03(Long tagInfo03) {
        this.tagInfo03 = tagInfo03;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserTagDTO{" +
                "userId=" + userId +
                ", tagInfo01=" + tagInfo01 +
                ", tagInfo03=" + tagInfo03 +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
