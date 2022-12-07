package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjs.examfrog.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaMapper extends BaseMapper<Area> {
    Area getOneForManage(Long id);

    Area searchForManage(@Param("name")String name);

    List<Area> listAllForManage();
}
