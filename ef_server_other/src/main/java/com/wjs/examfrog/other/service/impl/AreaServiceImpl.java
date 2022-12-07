package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.entity.Area;
import com.wjs.examfrog.other.mapper.AreaMapper;
import com.wjs.examfrog.other.mapper.UserPostMapper;
import com.wjs.examfrog.other.service.AreaService;
import com.wjs.examfrog.vo.AreaVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Resource
    AreaMapper areaMapper;
    @Resource
    UserPostMapper userPostMapper;

    @Override
    public List<AreaVO> listAllForManage() {
        return convertAreaVOs(areaMapper.listAllForManage());
    }

    public List<AreaVO> convertAreaVOs(List<Area> areaList){
        return areaList.parallelStream()
                .map(this::convertAreaVO)
                .collect(Collectors.toList());
    }

    public AreaVO convertAreaVO(Area area){
        AreaVO areaVO = new AreaVO();

        BeanUtil.copyProperties(area,areaVO);
        areaVO.setCount(userPostMapper.countNum(area.getId(),null));

        return areaVO;
    }
}
