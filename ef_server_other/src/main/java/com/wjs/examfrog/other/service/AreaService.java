package com.wjs.examfrog.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.entity.Area;
import com.wjs.examfrog.vo.AreaVO;

import java.util.List;


public interface AreaService extends IService<Area> {

    /**
     * 后台管理 获取分区
     * @return
     */
    List<AreaVO> listAllForManage();
}
