package com.aurora.live.id.generate.provider.model.bo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 有序 id 的 BO 对象
 *
 * @author halo
 * @since 2024/3/30 19:46
 */
public class LocalSeqIdBO {

    private Integer id;

    /**
     * 在内存中记录的当前有序id的值
     */
    private AtomicLong currentNum;

    /**
     * 当前id段的开始值
     */
    private Long currentStart;

    /**
     * 当前id段的结束值
     */
    private Long nextThreshold;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AtomicLong getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(AtomicLong currentNum) {
        this.currentNum = currentNum;
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

    @Override
    public String toString() {
        return "LocalSeqIdBO{" +
                "id=" + id +
                ", currentNum=" + currentNum +
                ", currentStart=" + currentStart +
                ", nextThreshold=" + nextThreshold +
                '}';
    }
}
