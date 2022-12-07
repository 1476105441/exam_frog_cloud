package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wjs.examfrog.other.service.HotListService;
import com.wjs.examfrog.other.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis 热榜实现类
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class HotListServiceImpl implements HotListService {

    @Resource
    private RedisService redisService;
    @Value("${redis.databaseName:databaseName}")
    private String redisDatabaseName;
    @Value("${redis.key.hotList:hotList}")
    private String hotList;
    @Value("${redis.key.perDayHotList:perDayHotList}")
    private String perDayHotList;
    @Value("${redis.key.hotListKeyIdx:hotListKeyIdx}")
    private String hotListKeyIdx;
    @Value("${redis.hotListSize:30}")
    private Integer hotListSize;

    @Override
    public void incrUserPost(Long userPostId) {
        Integer hotListIdx = getHotListIdx();

        String key1 = redisDatabaseName + ":" + hotList + ":" + hotListIdx;
        String key2 = redisDatabaseName + ":" + perDayHotList + ":" + hotListIdx;
        String value = "userPost" + ":" + userPostId;
        redisService.zincrby(key1, value, 1L);
        redisService.zincrby(key2, value, 1L);
    }

    @Override
    public List<Long> getHotList(Long size) {
        Integer cur = getHotListIdx();

        String key = redisDatabaseName + ":" + hotList + ":" + cur;
        String valuePrefix = "userPost";
        return redisService.zrangeByScore(key, 0, size).parallelStream()
                .map(x -> Long.parseLong(StrUtil.removePrefix((String) x, valuePrefix)))
                .collect(Collectors.toList());
    }

    @Override
    public void incrHotListIdx() {
        Integer cur = getHotListIdx();
        String curHotListKey = redisDatabaseName + ":" + hotList + ":" + cur;
        Set<ZSetOperations.TypedTuple<Object>> lastHotList = redisService.zrangeByScoreWithScore(curHotListKey, 0, -1);

        Integer next = (cur + 1) % hotListSize;
        String oldestPerDayHotListKey = redisDatabaseName + ":" + perDayHotList + ":" + next;
        Set<ZSetOperations.TypedTuple<Object>> oldestPerDayHotList = redisService.zrangeByScoreWithScore(oldestPerDayHotListKey, 0, -1);

        String nextHotListKey = redisDatabaseName + ":" + hotList + ":" + next;
        // 清除 上一个周期 这天的hotList数据
        redisService.zremRangeByRank(nextHotListKey, 0, -1);
        // 加上昨天的数据
        lastHotList.forEach(x -> {
            String value = (String) x.getValue();
            Long score = Math.round(x.getScore());
            redisService.zincrby(nextHotListKey, value, score);
        });
        // 减去前 上一个周期 这天的数据
        oldestPerDayHotList.forEach(x -> {
            String value = (String) x.getValue();
            Long score = Math.round(x.getScore());
            redisService.zincrby(nextHotListKey, value, -score);
        });
        // 清除 分数为 0 的数据
        redisService.zremRangeByScore(nextHotListKey, 0, 0);
        // 清除 上一个周期 这天的perDayHotList数据
        redisService.zremRangeByRank(oldestPerDayHotListKey, 0, -1);

        // 修改 hotListKeyIdx
        String key = redisDatabaseName + ":" + hotListKeyIdx;
        redisService.set(key, next);
    }

    private Integer getHotListIdx() {
        String key = redisDatabaseName + ":" + hotListKeyIdx;
        Integer cur = (Integer) redisService.get(key);
        if (cur == null) {
            redisService.set(key, 0);
            cur = 0;
        }

        return cur;
    }

}
