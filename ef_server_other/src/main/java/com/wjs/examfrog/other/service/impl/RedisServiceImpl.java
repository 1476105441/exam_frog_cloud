package com.wjs.examfrog.other.service.impl;

import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作Service的实现类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * value 操作
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value);
        this.expire(key, expire);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key,delta);
    }


    /**
     * zset 操作
     */
    @Override
    public void zadd(String key, Object value, Long score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zincrby(String key, Object value, Long delta) {
        redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    @Override
    public Set<Object> zrangeByScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> zrangeByScoreWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    @Override
    public void zremRangeByScore(String key, long min, long max) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, min);
    }

    @Override
    public void zremRangeByRank(String key, long start, long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }


    /**
     * 通用操作
     */
    @Override
    public boolean expire(String key, long expire) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, expire, TimeUnit.SECONDS));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void removeByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys == null) {
            throw new ApiException(ResultCode.INTERNAL_SERVER_ERROR, "Redis异常");
        }
        redisTemplate.delete(keys);
    }

}
