package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.dto.UrlDTO;
import com.wjs.examfrog.entity.ExcellentPost;
import com.wjs.examfrog.entity.Qualification;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.mapper.ExcellentPostMapper;
import com.wjs.examfrog.other.mapper.UserPostMapper;
import com.wjs.examfrog.other.service.ExcellentPostService;
import com.wjs.examfrog.other.service.QualificationService;
import com.wjs.examfrog.other.service.UserPostService;
import com.wjs.examfrog.vo.ExcellentPostVO;
import com.wjs.examfrog.vo.UserPostVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExcellentPostServiceImpl extends ServiceImpl<ExcellentPostMapper, ExcellentPost> implements ExcellentPostService {
    @Resource
    ExcellentPostMapper excellentPostMapper;
    @Resource
    UserPostService userPostService;
    @Resource
    UserPostMapper userPostMapper;
    @Resource
    QualificationService qualificationService;

    @Override
    public Page listExcellentByManage(Page<ExcellentPost> page) {
        return excellentPostMapper.listExcellentByManage(page);
    }

    public List<ExcellentPostVO> convertExcellentPostVOs(List<ExcellentPost> excellentPostList){
        return excellentPostList.parallelStream()
                .map(this::convertExcellentPostVO)
                .collect(Collectors.toList());
    }

    @Override
    public ExcellentPost getOneByManage(Long id) {
        return excellentPostMapper.getOneByManage(id);
    }

    @Override
    public Boolean updateStatus(ExcellentPost post) {
        return excellentPostMapper.updateStatus(post);
    }

    public ExcellentPostVO convertExcellentPostVO(ExcellentPost excellentPost){
        UserPostVO userPostVO = userPostService.convertUserPostVO(userPostMapper.getOneForManage(excellentPost.getPostId()));

        if (userPostVO == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"牛蛙内容不存在");
        }
        ExcellentPostVO excellentPostVO = new ExcellentPostVO();
        BeanUtil.copyProperties(userPostVO,excellentPostVO);
        BeanUtil.copyProperties(excellentPost,excellentPostVO);

        //处理审核状态
        if(!excellentPost.getIsExamine()){
            if (excellentPost.getIsDelete()) {
                excellentPostVO.setStatus("驳回");
            }else{
                excellentPostVO.setStatus("待审核");
            }
        }else{
            if (excellentPost.getIsDelete()) {
                excellentPostVO.setStatus("已下线");
            }else{
                excellentPostVO.setStatus("已上线");
            }
        }

        //添加认证资料
        List<UrlDTO> urlDTOS = qualificationService
                .list(new QueryWrapper<Qualification>().eq("excellent_post_id", excellentPost.getId()))
                .parallelStream()
                .map(x -> new UrlDTO(null, x.getUrl()))
                .collect(Collectors.toList());
        excellentPostVO.setUrlList(urlDTOS);

        return excellentPostVO;
    }
}
