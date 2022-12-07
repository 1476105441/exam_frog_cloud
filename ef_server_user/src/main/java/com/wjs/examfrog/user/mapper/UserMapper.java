package com.wjs.examfrog.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dingdong
 * @since 2021-07-16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
