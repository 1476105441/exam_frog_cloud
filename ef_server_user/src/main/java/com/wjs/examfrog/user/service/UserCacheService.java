package com.wjs.examfrog.user.service;

import com.wjs.examfrog.entity.User;


/**
 * user 缓存操作类
 */
public interface UserCacheService {

    /**
     * 保存
     *
     * @param user
     */
    void setUserByOpenId(User user);

    /**
     * 查询
     *
     * @param openId
     * @return
     */
    User getUserByOpenId(String openId);

    /**
     * 删除
     *
     * @param openId
     */
    void delUserByOpenId(String openId);

    /**
     * 删除全部
     */
    void delAllUser();

}
