package com.aurora.live.id.generate.provider.service.impl;

import com.aurora.live.id.generate.provider.dao.entity.IdGenerateDO;
import com.aurora.live.id.generate.provider.dao.mapper.IdGenerateMapper;
import com.aurora.live.id.generate.provider.model.bo.LocalSeqIdBO;
import com.aurora.live.id.generate.provider.model.bo.LocalUnSeqIdBO;
import com.aurora.live.id.generate.provider.service.IdGenerateService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * IdGenerateServiceImpl
 *
 * @author halo
 * @since 2024/3/30 00:14
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    @Resource
    private IdGenerateMapper idGenerateMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateServiceImpl.class);

    /**
     * 本地有序 id 段缓存 map
     */
    private static final Map<Integer, LocalSeqIdBO> LOCAL_SEQ_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 本地无序 id 段缓存 map
     */
    private static final Map<Integer, LocalUnSeqIdBO> LOCAL_UN_SEQ_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 自定义线程池
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                return thread;
            });

    /**
     * 更新阈值百分比
     */
    private static final float UPDATE_RATE = 0.75f;

    /**
     * 剩余可用空间百分比
     */
    private static final float CAN_USE_SPACE_RATE = 0.25f;

    /**
     * id 是否有序
     */
    private static final int SEQ_ID = 1;

    /**
     * 使用信号量控制对关键部分的访问，确保只有有限数量的线程可以同时执行某些操作。
     */
    private static final Map<Integer, Semaphore> SEMAPHORE_MAP = new ConcurrentHashMap<>();

    /**
     * 生成有序 id, 采用了局部有序性去进行设计，不能保证id段完全用完
     *
     * @param id 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    @Override
    public Long getSeqId(Integer id) {
        if (Objects.isNull(id)) {
            LOGGER.error("[getSeqId] id is error, id is {}", id);
            return null;
        }
        LocalSeqIdBO localSeqIdBO = LOCAL_SEQ_ID_MAP.get(id);
        if (Objects.isNull(localSeqIdBO)) {
            LOGGER.error("[getSeqId] localSeqIdBO is null, id is {}", id);
            return null;
        }
        this.refreshLocalSeqId(localSeqIdBO);
        long returnId = localSeqIdBO.getCurrentNum().incrementAndGet();
        if (returnId > localSeqIdBO.getNextThreshold()) {
            // 同步刷新
            LOGGER.error("[getSeqId] id is over limit, id is {}", id);
            return null;
        }
        return returnId;
    }

    /**
     * 生成无序 id
     *
     * @param id 业务 ID 生成策略的主键
     * @return 分布式 id
     */
    @Override
    public Long getUnSeqId(Integer id) {
        if (Objects.isNull(id)) {
            LOGGER.error("[getSeqId] id is error,id is {}", id);
            return null;
        }
        LocalUnSeqIdBO localUnSeqIdBO = LOCAL_UN_SEQ_ID_MAP.get(id);
        if (Objects.isNull(localUnSeqIdBO)) {
            LOGGER.error("[getUnSeqId] localUnSeqIdBO is null, id is {}", id);
            return null;
        }
        Long returnId = localUnSeqIdBO.getIdQueue().poll();
        if (returnId == null) {
            LOGGER.error("[getUnSeqId] returnId is null, id is {}", id);
            return null;
        }
        this.refreshLocalUnSeqId(localUnSeqIdBO);
        LOGGER.info("发放 ID：{}", returnId);
        return returnId;
    }

    /**
     * bean 初始化的时候会回调方法
     */
    @Override
    public void afterPropertiesSet() {
        List<IdGenerateDO> idGenerateDOList = idGenerateMapper.selectList(null);
        for (IdGenerateDO entity : idGenerateDOList) {
            LOGGER.info("服务启动，抢占新的 id 段");
            tryUpdateMysqlRecord(entity);
            SEMAPHORE_MAP.put(entity.getId(), new Semaphore(1));
        }
    }

    /**
     * 刷新本地有序 id 段
     *
     * @param localSeqIdBO 本地有序 id 生成器业务对象
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        if (localSeqIdBO.getCurrentNum().get() - localSeqIdBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = SEMAPHORE_MAP.get(localSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null,id is {}", localSeqIdBO.getId());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("开始尝试进行本地id段的同步操作");
                // 异步进行同步id段操作
                THREAD_POOL_EXECUTOR.execute(() -> {
                    try {
                        IdGenerateDO idGenerateDO = idGenerateMapper.selectById(localSeqIdBO.getId());
                        tryUpdateMysqlRecord(idGenerateDO);
                    } catch (Exception e) {
                        LOGGER.error("[refreshLocalSeqId] error is ", e);
                    } finally {
                        SEMAPHORE_MAP.get(localSeqIdBO.getId()).release();
                        LOGGER.info("本地有序id段同步完成,id is {}", localSeqIdBO.getId());
                    }
                });
            }
        }
    }

    /**
     * 刷新本地无序 id 段
     *
     * @param localUnSeqIdBO 本地无序 id 生成器对象
     */
    private void refreshLocalUnSeqId(LocalUnSeqIdBO localUnSeqIdBO) {
        long begin = localUnSeqIdBO.getCurrentStart();
        long end = localUnSeqIdBO.getNextThreshold();
        long remainSize = localUnSeqIdBO.getIdQueue().size();
        // 如果使用剩余空间不足25%，则进行刷新
        if ((end - begin) * CAN_USE_SPACE_RATE > remainSize) {
            Semaphore semaphore = SEMAPHORE_MAP.get(localUnSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null, id is {}", localUnSeqIdBO.getId());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                THREAD_POOL_EXECUTOR.execute(() -> {
                    try {
                        IdGenerateDO idGenerateDO = idGenerateMapper.selectById(localUnSeqIdBO.getId());
                        tryUpdateMysqlRecord(idGenerateDO);
                    } catch (Exception e) {
                        LOGGER.error("[refreshLocalUnSeqId] error is ", e);
                    } finally {
                        SEMAPHORE_MAP.get(localUnSeqIdBO.getId()).release();
                        LOGGER.info("本地无序 id 段同步完成，id is {}", localUnSeqIdBO.getId());
                    }
                });
            }
        }
    }

    /**
     * 更新 mysql 里面的分布式 id 的配置信息，占用相应的 id 段
     * 同步执行，很多的网络IO，性能较慢
     *
     * @param idGenerateDO 业务 ID 生成策略实例
     */
    private void tryUpdateMysqlRecord(IdGenerateDO idGenerateDO) {
        int updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGenerateDO.getId(), idGenerateDO.getVersion());
        if (updateResult > 0) {
            localIdBizObjHandler(idGenerateDO);
            return;
        }
        // 重试进行更新
        for (int i = 0; i < 3; i++) {
            idGenerateDO = idGenerateMapper.selectById(idGenerateDO.getId());
            updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGenerateDO.getId(), idGenerateDO.getVersion());
            if (updateResult > 0) {
                localIdBizObjHandler(idGenerateDO);
                return;
            }
        }
        throw new RuntimeException("表id段占用失败，竞争过于激烈，id is " + idGenerateDO.getId());
    }

    /**
     * 专门处理如何将本地 ID 对象放入到 Map 中，并且进行初始化的
     *
     * @param idGenerateDO 业务 ID 生成策略实例
     */
    private void localIdBizObjHandler(IdGenerateDO idGenerateDO) {
        long currentStart = idGenerateDO.getCurrentStart();
        long nextThreshold = idGenerateDO.getNextThreshold();
        long currentNum = currentStart;
        if (idGenerateDO.getIsSeq() == SEQ_ID) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            AtomicLong atomicLong = new AtomicLong(currentNum);
            localSeqIdBO.setId(idGenerateDO.getId());
            localSeqIdBO.setCurrentNum(atomicLong);
            localSeqIdBO.setCurrentStart(currentStart);
            localSeqIdBO.setNextThreshold(nextThreshold);
            LOCAL_SEQ_ID_MAP.put(localSeqIdBO.getId(), localSeqIdBO);
        } else {
            LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
            localUnSeqIdBO.setCurrentStart(currentStart);
            localUnSeqIdBO.setNextThreshold(nextThreshold);
            localUnSeqIdBO.setId(idGenerateDO.getId());
            long begin = localUnSeqIdBO.getCurrentStart();
            long end = localUnSeqIdBO.getNextThreshold();
            List<Long> idList = new ArrayList<>();
            for (long i = begin; i < end; i++) {
                idList.add(i);
            }
            // 将本地id段提前打乱，然后放入到队列中
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>(idList);
            localUnSeqIdBO.setIdQueue(idQueue);
            LOCAL_UN_SEQ_ID_MAP.put(localUnSeqIdBO.getId(), localUnSeqIdBO);
        }
    }
}
