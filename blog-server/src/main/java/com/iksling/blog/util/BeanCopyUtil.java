package com.iksling.blog.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtil {
    public static <T> T copyObject(Object source, Class<T> target) {
        T obj = null;
        try {
            obj = target.newInstance();
            if (null != source)
                BeanUtils.copyProperties(source, obj);
        } catch (IllegalAccessException | InstantiationException | BeansException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source && source.size() > 0) {
            for (Object obj : source)
                list.add(BeanCopyUtil.copyObject(obj, target));
        }
        return list;
    }
}
