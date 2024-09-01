package com.iksling.blog.util;

import com.iksling.blog.exception.OperationStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static <T> void setValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static <T> void setValue(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public static <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public static <T> Long setList(String key, List<T> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    public static <T> List<T> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public static <T> void setSet(String key, Set<T> value) {
        value.forEach(redisTemplate.boundSetOps(key)::add);
    }

    public static <T> void setSetValue(String key, T value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    public static <T> Set<T> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public static <T> void setMap(String key, Map<String, T> value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    public static <T> void setMapValue(String key, String hKey, T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    public static <T> Map<String, T> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public static <T> Map<String, T> getMaps(String keyPattern) {
        return (Map<String, T>) redisTemplate.execute((RedisCallback<Map<String, T>>) connection -> {
            Map<String, T> map = new HashMap<>();
            try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(keyPattern).count(10000).build())) {
                while (cursor.hasNext()) {
                    map.putAll(RedisUtil.getMap(new String(cursor.next(), StandardCharsets.UTF_8)));;
                }
            } catch (Exception e) {
                throw new OperationStatusException();
            }
            return map;
        });
    }

    public static <T> T getMapValue(String key, String hKey) {
        return (T) redisTemplate.opsForHash().get(key, hKey);
    }

    public static Boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    public static Long delKey(Collection keys) {
        return redisTemplate.delete(keys);
    }

    public static Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    public static Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    public static Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    public static void increment(String key, long value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    public static void increment(String key, String hKey, long value) {
        redisTemplate.opsForHash().increment(key, hKey, value);
    }
}
