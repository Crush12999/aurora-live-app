package com.aurora.live.id.generate.provider.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * ID 生成器配置类
 *
 * @author halo
 * @since 2024/3/30 00:07
 */
@TableName("t_id_generate_config")
public class IdGenerateDO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * ID 备注描述
     */
    private String remark;

    /**
     * 初始化值
     */
    private Long initNum;

    /**
     * 步长
     */
    private Integer step;

    /**
     * 是否是有序的 id
     */
    private Integer isSeq;

    /**
     * 当前 id 所在阶段的开始值
     */
    private Long currentStart;

    /**
     * 当前 id 所在阶段的阈值
     */
    private Long nextThreshold;

    /**
     * 业务代码前缀
     */
    private String idPrefix;

    /**
     * 乐观锁版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getInitNum() {
        return initNum;
    }

    public void setInitNum(Long initNum) {
        this.initNum = initNum;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getIsSeq() {
        return isSeq;
    }

    public void setIsSeq(Integer isSeq) {
        this.isSeq = isSeq;
    }

    public Long getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(Long currentStart) {
        this.currentStart = currentStart;
    }

    public Long getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(Long nextThreshold) {
        this.nextThreshold = nextThreshold;
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
        return "IdGenerateDO{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", initNum=" + initNum +
                ", step=" + step +
                ", isSeq=" + isSeq +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                ", idPrefix='" + idPrefix + '\'' +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
