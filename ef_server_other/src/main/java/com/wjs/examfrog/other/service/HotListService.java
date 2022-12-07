package com.wjs.examfrog.other.service;

import java.util.List;


public interface HotListService {

    /**
     * 加一
     *
     * @param userPostId
     */
    void incrUserPost(Long userPostId);

    /**
     * 获取榜单
     *
     * @param size
     * @return
     */
    List<Long> getHotList(Long size);

    /**
     * 下一天
     */
    void incrHotListIdx();

}
