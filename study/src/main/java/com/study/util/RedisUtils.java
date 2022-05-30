package com.study.util;/*
package study.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import study.config.ServerConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

*/
/**
 * @Author : luolan
 * @Date: 2022-03-16 14:25
 * @Description :
 *//*

@Slf4j
@Component
public class RedisUtils {

    @Resource(name = "studyRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // 设置分布式锁过期时间
    public static final int LOCK_EXPIRE = 300;

    // 分布式锁前缀
    public static final String LOCK_PREFIX = "lock:";

    public static final String KEY_SPLIT = ":";

    //  ===============================通过redisTemplate操作=====================
    */
/**
     * 删除缓存
     * @param key 可以传一个值或多个
     *//*

    public void del(String... key){
        if(key != null && key.length > 0){
            if(key.length ==1 ){
                redisTemplate.delete(key[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    */
/**
     * 设置缓存失效时间
     * @param key 键
     * @param time 过期时间（秒）
     *//*

    public boolean expire(String key, long time){
        try{
            if(time > 0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    */
/**
     * 获取缓存过期时间
     * @param key 键
     * @return 时间（秒） 返回0代表永久有效
     *//*

    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    */
/**
     * 判断key是否存在
     * @param key 键
     * @return true存在 false不存在
     *//*

    public boolean hasKey(String key) {
        try{
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //  ===============================String=====================

    */
/**
     * 通过key获取
     * @param key 键
     * @return 值
     *//*

    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    */
/**
     * 存储key-value
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     *//*

    public boolean set(String key, String value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    */
/**
     * 存储key-value并设置过期时间
     * @param key 键
     * @param value 值
     * @param time 过期时间（秒）
     * @return true成功 false失败
     *//*

    public boolean set(String key, String value, long time){
        try{
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    */
/**
     * 递增
     * @param key 键
     * @param delta 递增因子（要增加几，大于0）
     * @return
     *//*

    public long increment(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    */
/**
     * 递减
     * @param key 键
     * @param delta 递减因子（要减几，小于0）
     * @return
     *//*

    public long decrement(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递减因子必须小于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    //  ===============================hashmap=====================

    */
/**
     * hashGet
     * @param key 键
     * @param hashKey 项
     * @return
     *//*

    public Object get(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    */
/**
     * 取出hash中的全部值
     * @param key 键 不能为null
     * @return
     *//*

    public Object entries(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    */
/**
     * 普通hash存储
     * @param key 键
     * @param hashKey 项
     * @param value 值
     *//*

    public void put(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    */
/**
     * 批量hash存储
     * @param key 键
     * @param map hashkey-value组
     *//*

    public void putAll(String key, Map map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    */
/**
     * hash批量删除
     * @param key 键
     * @param hashKeys 任意个hashkey
     *//*

    public long delete(String key, Object... hashKeys){
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    //  ===============================List=====================

    */
/**
     * 左边放入
     * @param key
     * @param value
     * @return
     *//*

    public long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    */
/**
     * 左边批量放入
     * @param key
     * @param values
     * @return
     *//*

    public long leftPushAll(String key, Object... values){
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    */
/**
     * 左边移除
     * @param key
     * @return
     *//*

    public Object leftPop(String key){
        return redisTemplate.opsForList().leftPop(key);
    }

    */
/**
     * 右边放入
     * @param key
     * @param value
     * @return
     *//*

    public long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    */
/**
     * 右边移除
     * @param key
     * @return
     *//*

    public Object rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    */
/**
     * 获取List中元素
     * @param key  键
     * @param start 开始（元素下标，从0开始）
     * @param end 结束（元素下标，获取全部该值为-1）
     * @return
     *//*

    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public static String generateKey(String key){
        if(StringUtil.isNull(key) ){
            throw new RuntimeException("");
        }
        return generateKey(key, null);
    }

    public static String generateKey(String key, String year){
        if(StringUtil.isNull(year)){
            year = String.valueOf(DateUtils.getCurrentYear());
        }
        return new StringBuilder(year).append(KEY_SPLIT).append(key).toString();
    }

    */
/**
     * 获取分布式锁
     * 整体思路是：
     * 1. 使用setNX，判断是否存在分布式锁，如果不存在则设置超时时间戳，然后返回true
     * 2. 如果存在，则校验超时时间戳是否超时，如果超时，则设置新的超时时间戳
     * 3. 此处的分布式锁每LOCK_EXPIRE释放一次，不管其他线程是否已经完成查询操作或者死锁
     * @param key
     * @return
     *//*

    public boolean getDistributeLock(String key){
        String lockKey = RedisUtils.generateKey(LOCK_PREFIX + key);
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(expireAt).getBytes());
            // 设置超时时间，避免长时间占用内存,除100而不是除1000，主要原因在于设置超时时间长于过期时间
            connection.expire(lockKey.getBytes(), LOCK_EXPIRE/100);
            if (acquire) {
                return true;
            } else {
                byte[] value = connection.get(lockKey.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        // 防止死锁,再次确认是否可以拿到锁，如果此时被其他线程抢占，则返回false
                        return Long.parseLong(new String(connection.getSet(lockKey.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes()))) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

}
*/
