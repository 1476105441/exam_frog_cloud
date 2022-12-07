package com.wjs.examfrog.other.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjs.examfrog.entity.ExcellentPost;
import com.wjs.examfrog.vo.ExcellentPostVO;

import java.util.List;


public interface ExcellentPostService extends IService<ExcellentPost> {
    Page listExcellentByManage(Page<ExcellentPost> page);

    ExcellentPostVO convertExcellentPostVO(ExcellentPost excellentPost);

    List<ExcellentPostVO> convertExcellentPostVOs(List<ExcellentPost> excellentPostList);

    ExcellentPost getOneByManage(Long id);

    Boolean updateStatus(ExcellentPost post);
}
