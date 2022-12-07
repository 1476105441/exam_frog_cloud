package com.wjs.examfrog.other.controller;

import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import com.wjs.examfrog.other.service.AreaService;
import com.wjs.examfrog.vo.AreaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "后台管理——分区")
@RestController()
public class AreaController {

    @Resource
    private AreaService areaService;

    @ApiOperation(value = "后台管理 获取全部分区")
    @GetMapping("/area/listAll")
    public ResponseEntity<Result<List<AreaVO>>> listAllForManage(){
        List<AreaVO> areaVOS = areaService.listAllForManage();
        return CommonResult.success(areaVOS);
    }
}
