package com.wjs.examfrog.user.service.impl;

import com.wjs.examfrog.annotation.CacheException;
import com.wjs.examfrog.entity.User;
import com.wjs.examfrog.user.service.RedisService;
import com.wjs.examfrog.user.service.UserCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User 缓存实现类
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {

    @Resource
    private RedisService redisService;
    /**
     * 默认存 7 天
     */
    @Value("${redis.expire.default:604800}")
    private long expire;
    @Value("${redis.databaseName:databaseName}")
    private String redisDatabaseName;
    @Value("${redis.key.user:user}")
    private String keyUser;

    @CacheException
    @Override
    public void setUserByOpenId(User user) {
        String prefix = redisDatabaseName + ":" + keyUser;
        String key = prefix + ":" + user.getOpenId();
        redisService.setWithExpire(key, user, expire);
    }

    @CacheException
    @Override
    public User getUserByOpenId(String openId) {
        String prefix = redisDatabaseName + ":" + keyUser;
        String key = prefix + ":" + openId;
        return (User) redisService.get(key);
    }

    @CacheException
    @Override
    public void delUserByOpenId(String openId) {
        String prefix = redisDatabaseName + ":" + keyUser;
        String key = prefix + ":" + openId;
        redisService.remove(key);
    }

    @CacheException
    @Override
    public void delAllUser() {
        String prefix = redisDatabaseName + ":" + keyUser;
        redisService.removeByPrefix(prefix);
    }

}
