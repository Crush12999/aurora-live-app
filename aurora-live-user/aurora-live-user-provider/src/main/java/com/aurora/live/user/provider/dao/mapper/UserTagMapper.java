package com.aurora.live.user.provider.dao.mapper;

import com.aurora.live.user.provider.dao.entity.UserTagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户标签 mapper
 *
 * @author halo
 * @since 2024/4/1 23:31
 */
@Mapper
public interface UserTagMapper extends BaseMapper<UserTagDO> {

    /**
     * 设置标签，只允许第一次设置成功
     *
     * @param userId    用户 id
     * @param fieldName 要设置的标签字段
     * @param tag       标签值
     * @return 设置结果
     */
    @Update("""
                update t_user_tag 
                    set ${fieldName}=${fieldName} | #{tag} 
                where user_id = #{userId} 
                    and ${fieldName} & #{tag}=0
            """)
    int setTag(Long userId, String fieldName, long tag);

    /**
     * 取消标签，只允许第一次删除成功
     *
     * @param userId    用户 id
     * @param fieldName 要设置的标签字段
     * @param tag       标签值
     * @return 取消结果
     */
    @Update("""
                update t_user_tag 
                    set ${fieldName}=${fieldName} &~ #{tag} 
                where user_id = #{userId} 
                    and ${fieldName} & #{tag}=#{tag}
            """)
    int cancelTag(Long userId, String fieldName, long tag);
}
