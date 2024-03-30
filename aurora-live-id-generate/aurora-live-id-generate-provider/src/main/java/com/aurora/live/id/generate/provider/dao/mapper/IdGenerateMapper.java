package com.aurora.live.id.generate.provider.dao.mapper;

import com.aurora.live.id.generate.provider.dao.entity.IdGenerateDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * IdGenerateMapper
 *
 * @author halo
 * @since 2024/3/30 00:06
 */
@Mapper
public interface IdGenerateMapper extends BaseMapper<IdGenerateDO> {

    /**
     * 通过乐观锁抢占 id 段
     *
     * @param id      业务 id 生成器配置主键
     * @param version 版本号
     * @return 更新行数
     */
    @Update("""
        update 
            t_id_generate_config set next_threshold=next_threshold+step,
            current_start=current_start+step,
            version=version+1 
        where id =#{id} and version=#{version}
    """)
    int updateNewIdCountAndVersion(@Param("id") Integer id, @Param("version") Integer version);
}
