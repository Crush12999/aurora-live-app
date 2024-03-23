package com.aurora.live.common.interfaces;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象转换工具类
 *
 * @author halo
 * @since 2024/3/23 22:23
 */
public class ConvertBeanUtils {

    /**
     * 将一个对象转成目标对象
     *
     * @param source      源对象
     * @param targetClass 目标对象类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T t = newInstance(targetClass);
        BeanUtils.copyProperties(source, t);
        return t;
    }


    /**
     * 将List对象转换成目标对象，注意实现是ArrayList
     *
     * @param sourceList  源数组
     * @param targetClass 目标对象类型
     * @return 目标对象列表
     */
    public static <K, T> List<T> convertList(List<K> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>((int) (sourceList.size() / 0.75) + 1);
        for (K source : sourceList) {
            targetList.add(convert(source, targetClass));
        }
        return targetList;
    }

    private static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeanInstantiationException(targetClass, "instantiation error", e);
        }
    }
}
