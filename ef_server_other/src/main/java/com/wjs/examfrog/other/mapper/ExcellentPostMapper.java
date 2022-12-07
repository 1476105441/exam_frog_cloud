package com.wjs.examfrog.other.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjs.examfrog.dto.PostManageDTO;
import com.wjs.examfrog.entity.ExcellentPost;
import com.wjs.examfrog.entity.UserPost;
import org.apache.ibatis.annotations.Param;

public interface ExcellentPostMapper extends BaseMapper<ExcellentPost> {
    /**
     * 后台管理 牛蛙内容-牛蛙审核全部查询
     * @param page
     * @return
     */
    Page<ExcellentPost> listExcellentByManage(Page<ExcellentPost> page);

    /**
     * 后台管理 牛蛙内容-获取牛蛙审核详情
     * @param id
     * @return
     */
    ExcellentPost getOneByManage(Long id);

    /**
     * 后台管理 牛蛙内容-更新牛蛙帖子状态
     * @param post
     * @return
     */
    Boolean updateStatus(ExcellentPost post);

    /**
     * 后台管理 牛蛙内容-通过牛蛙的用户帖子获得牛蛙审核内容
     * @param postId
     * @return
     */
    ExcellentPost getOneByPostId(Long postId);

    /**
     * 后台管理 用户文章-搜索牛蛙分区
     * @param page
     * @param post
     * @return
     */
    Page<UserPost> searchExcellentPost(Page page,@Param("post") PostManageDTO post);
}
