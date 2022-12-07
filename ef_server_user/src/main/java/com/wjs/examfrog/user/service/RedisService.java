package com.wjs.examfrog.user.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * redis操作Service,
 * 对象和数组都以json形式进行存储
 */
public interface RedisService {

    /**
     * value 操作
     */
    void set(String key, Object value);

    void setWithExpire(String key, Object value, long expire);

    Object get(String key);

    Long increment(String key, long delta);


    /**
     * zset 操作
     */
    void zadd(String key, Object value, Long score);

    void zincrby(String key, Object value, Long delta);

    Set<Object> zrangeByScore(String key, long start, long end);

    Set<ZSetOperations.TypedTuple<Object>> zrangeByScoreWithScore(String key, long start, long end);

    void zremRangeByScore(String key, long min, long max);

    void zremRangeByRank(String key, long start, long end);


    /**
     * 通用操作
     */
    boolean expire(String key, long expire);

    void remove(String key);

    void removeByPrefix(String prefix);

}
