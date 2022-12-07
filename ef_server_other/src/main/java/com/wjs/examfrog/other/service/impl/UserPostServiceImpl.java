package com.wjs.examfrog.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.*;
import com.wjs.examfrog.entity.*;
import com.wjs.examfrog.entity.UserPost;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.other.mapper.ExcellentPostMapper;
import com.wjs.examfrog.other.mapper.UserPostMapper;
import com.wjs.examfrog.other.service.*;
import com.wjs.examfrog.vo.*;
//import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional(rollbackFor = Exception.class)
@Service
public class UserPostServiceImpl extends ServiceImpl<UserPostMapper, UserPost> implements UserPostService {

    @Resource
    private SelfIdGenerator selfIdGenerator;
    @Resource
    private CategoryService categoryService;
    @Resource
    private SubtaskService subtaskService;
    @Resource
    private ApiService apiService;
    @Resource
    private HotListService hotListService;
    @Resource
    private FavUserPostService favUserPostService;
    @Resource
    private UserPostMapper userPostMapper;
    @Resource
    private ExcellentPostService excellentPostService;
    @Resource
    private ExcellentPostMapper excellentPostMapper;
    @Resource
    private AreaService areaService;

    @Override
    public List<UserPostVO> listAllUserPosts() {
        List<UserPost> userPostList = this.list();
        return this.convertUserPostVOs(userPostList);
    }

    public List<UserPost> listUserPostByUserId(Long userId){
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<UserPost>().eq("author_id",userId);

        return this.list(queryWrapper);
    }

    @Override
    public UserPost getOneById(Long userPostId) {
        return this.getById(userPostId);
    }

    @Override
    public Boolean updateOneById(UserPost userPost) {
        return this.updateById(userPost);
    }

    @Override
    public Page listUserPosts(List<Long> categoryIdList, PageParamDTO pageParamDTO) {
        // 设置分页
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 没有筛选条件, 返回全部
        if (categoryIdList == null){
            Page<UserPost> page = this.page(userPostPage);
            return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(this.convertUserPostVOs(page.getRecords()))
                    .setTotal(page.getTotal());
        }

        // 构造搜索条件
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        // 处理 categoryIdList
        for (Long categoryId : categoryIdList) {
            if (categoryId != null) {
                queryWrapper.eq("category_id", categoryId).or();
            }
        }

        // 查询
        Page<UserPost> page = this.page(userPostPage,queryWrapper);
        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listUserPostsByUserId(Long userId, PageParamDTO pageParamDTO) {
        // 设置分页
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 构造条件
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<UserPost>()
                .eq("author_id", userId);

        Page<UserPost> page = this.page(userPostPage,queryWrapper);

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO) {
        // 分页
        Page<FavUserPost> favUserPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        QueryWrapper<FavUserPost> favQueryWrapper = new QueryWrapper<FavUserPost>()
                .eq("user_id", userId).orderByDesc("gmt_create");

        Page<FavUserPost> page = favUserPostService.page(favUserPostPage,favQueryWrapper);
        List<FavUserPost> favUserPostList = page.getRecords();

        // 如果没有就返回空数组
        if (favUserPostList.isEmpty()) {
            return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(new ArrayList<>())
                    .setTotal(page.getTotal());
        }

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (FavUserPost favUserPost : favUserPostList) {
            queryWrapper.eq("id", favUserPost.getUserPostId()).or();
        }

        // 查询
        List<UserPost> userPostList = new ArrayList<>(this.list(queryWrapper));

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(userPostList))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listUserPostById(PageParamDTO pageParamDTO, Long userId, int orderType) {
        // 分页
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",userId);

        //根据orderType做出不同的排序
        if (orderType == 0) {
            queryWrapper.orderByDesc("gmt_create");
        } else if (orderType == 1) {
            queryWrapper.orderByDesc("view_count");
        } else if (orderType == 2) {
            queryWrapper.orderByDesc("trying_count");
        }else {
            throw new ApiException(ResultCode.BAD_REQUEST,"查询条件有误");
        }

        Page<UserPost> page = this.page(userPostPage,queryWrapper);

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Boolean increaseSuccessCount(Long userPostId) {
        UserPost parentDB = this.getById(userPostId);
        if (parentDB != null) {
            UserPost parent = new UserPost();
            parent.setId(parentDB.getId());
            parent.setSuccessCount(parentDB.getSuccessCount() + 1);
            return this.updateById(parent);
        } else
            return false;
    }

    @Override
    public List<UserPostVO> getHotList(Long size) {
        List<Long> hotUserPostIdList = hotListService.getHotList(size);

        // 如果 不存在 就返回空
        if (hotUserPostIdList == null || hotUserPostIdList.isEmpty()) {
            return this.convertUserPostVOs(new ArrayList<>());
        }

        // 构造搜索条件
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (Long userPostId : hotUserPostIdList) {
            if (userPostId != null) {
                queryWrapper.eq("user_post_id", userPostId).or();
            }
        }

        // 查询
        List<UserPost> userPostList = this.list(queryWrapper);

        return this.convertUserPostVOs(userPostList);
    }

    @Override
    public Page listUserPostsByQuery(PageParamDTO pageParamDTO, String words) {
        // 设置分页
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // 构造搜索条件
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<UserPost>()
                .like("title", words).or()
                .like("tags", words).or()
                .like("content", words);

        // 查询
        Page<UserPost> page = this.page(userPostPage,queryWrapper);

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    /**
     * ===========================详情===========================
     */
    @Override
    public UserPostDetailsVO detail(Long id) {
        // 获取 UserPost
        UserPost userPost = this.getById(id);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "帖子不存在");
        }

        // 访问量 + 1
        hotListService.incrUserPost(id);

        return convertUserPostDetailsVO(userPost);
    }

    @Override
    public Boolean publishUserPost(Long userId, UserPostParamDTO userPostParamDTO) {
        // 检查数据
        this.checkUserPostParamDTO(userPostParamDTO);

        // 获取用户数据
        User user = apiService.getUserById(userId).getBody().getData();

        // 生成 userPostId
        Long userPostId = selfIdGenerator.nextId(null);

        // 保存 subtaskList
        subtaskService.saveSubtasksByUserPostId(userPostId, userPostParamDTO.getSubtaskList());

        // 插入 userPost
        UserPost userPost = new UserPost();
        BeanUtil.copyProperties(userPostParamDTO, userPost);
        userPost.setId(userPostId);
        userPost.setTryingCount(0L);
        userPost.setSuccessCount(0L);
        userPost.setLikeCount(0L);
        userPost.setFavCount(0L);
        userPost.setShareCount(0L);
        userPost.setAuthorAvatarUrl(user.getAvatarUrl());
        userPost.setViewCount(0L);
        userPost.setCommentCount(0L);
        userPost.setIsAuthentic(false);
        userPost.setAuthorId(userId);
        userPost.setAuthorNickName(user.getNickName());

        return this.save(userPost);
    }


    @Override
    public Boolean updateUserPost(Long userId, Long userPostId, UserPostParamDTO userPostParamDTO) {
        // 检测帖子是否存在
        UserPost userPostDB = this.getById(userPostId);
        if (userPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "更新的帖子不存在");
        }
        // 检查帖子作者是否为 userId
        if (!userPostDB.getAuthorId().equals(userId)) {
            throw new ApiException(ResultCode.FORBIDDEN, "改别人帖子干嘛");
        }
        // 检查参数是否有问题
        this.checkUserPostParamDTO(userPostParamDTO);

        // 修改 subtasks
        subtaskService.updateSubtasksByUserPostId(userPostId, userPostParamDTO.getSubtaskList());

        // 更新 userPost
        UserPost userPost = new UserPost();
        BeanUtil.copyProperties(userPostParamDTO, userPost);
        userPost.setId(userPostId);
        // 继承原来的属性
        userPost.setTryingCount(null);
        userPost.setSuccessCount(null);
        userPost.setAuthorId(null);
        userPost.setIsAuthentic(null);

        return this.updateById(userPost);
    }

    /**
     *   ============================后台管理================================
     */
    @Override
    public Boolean removeUserPosts(Long userId, List<Long> userPostIdList) {
        // 检查
        if (userPostIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "删除的内容不能为空");
        }

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (Long userPostId : userPostIdList) {
            queryWrapper.eq("id", userPostId).or();
        }

        //管理员可以删除帖子
        RoleAdminRelation admin = apiService.getOne(userId).getBody().getData();

        // 检查删除的帖子是否为 userId的
        List<UserPost> userPostList = this.list(queryWrapper);
        for (UserPost userPost : userPostList) {
            if (!userId.equals(userPost.getAuthorId()) && admin == null) {
                throw new ApiException(ResultCode.FORBIDDEN, "删别人帖子干嘛");
            }
        }

        // 删除 SubtaskList
        if (admin == null) {
            subtaskService.removeSubtasksByUserPostId(userPostIdList);
        }

        return this.removeByIds(userPostIdList);
    }

    @Override
    //@GlobalTransactional
    public void onlineUserPost(Long userId,Long userPostId){
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("用户不合法");
        }
        UserPost userPost = userPostMapper.getOneForManage(userPostId);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"要上线的帖子不存在");
        }
        Area area = areaService.getById(userPost.getAreaId());

        //牛蛙帖子上线不一样
        if ("牛蛙经验".equals(area.getName())) {
            ExcellentPost excellentPost = excellentPostMapper.getOneByPostId(userPostId);
            if (excellentPost == null) {
                throw new ApiException(ResultCode.BAD_REQUEST,"要上线的帖子不存在");
            }
            controlStatus(userId,excellentPost.getId(),0L);
        }else{
            Integer res = userPostMapper.onlineUserPost(userPostId);
            if (res == 0) {
                throw new ApiException(ResultCode.BAD_REQUEST,"帖子上线失败");
            }
        }
    }

    @Override
    //@GlobalTransactional
    public void removeUserPostForManage(Long userId, Long userPostId) {
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("用户不合法");
        }

        UserPost userPost = userPostMapper.getOneForManage(userPostId);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"要下线的帖子不存在");
        }
        Area area = areaService.getById(userPost.getAreaId());

        if ("牛蛙经验".equals(area.getName())) {
            ExcellentPost excellentPost = excellentPostMapper.getOneByPostId(userPostId);
            if (excellentPost == null) {
                throw new ApiException(ResultCode.BAD_REQUEST,"要下线的帖子不存在");
            }
            controlStatus(userId,excellentPost.getId(),1L);
        }else{
            List<Long> idList = new ArrayList<>();
            idList.add(userPostId);
            this.removeUserPosts(userId,idList);
        }
    }

    @Override
    //@GlobalTransactional
    public void deleteUserPostForManage(Long userId, Long userPostId) {
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("用户不合法");
        }

        Boolean res = userPostMapper.deleteUserPostForManage(userPostId);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST,"要下线的帖子不存在");
        }
    }

    @Override
    public Page listBySearch(PageParamDTO pageParamDTO, PostManageDTO postManageDTO) {
        Long zone = postManageDTO.getZone();
        Page<UserPost> page = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        this.checkPostManageDTO(postManageDTO);

        if (zone == null) {
            //全部一起查询
            page = userPostMapper.searchAllPost(page, postManageDTO);
        } else if (zone == 0) {
            //青蛙乐园的查询
            Area area1 = areaService.getOne(new QueryWrapper<Area>().eq("name","青蛙乐园"));
            page = userPostMapper.searchCommonPost(page, postManageDTO, area1.getId());
        } else if (zone == 1) {
            //牛蛙经验的帖子查询
            page = excellentPostMapper.searchExcellentPost(page, postManageDTO);
        }else{
            throw new ApiException(ResultCode.BAD_REQUEST,"分区不合法");
        }

        return new Page<UserPostManageVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostManageVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void checkPostManageDTO(PostManageDTO postManageDTO) {
        //检查日期是否合法
        String date = postManageDTO.getDate();
        if (!"".equals(date)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                //检查是否为年月日格式
                format.parse(date);
            } catch (ParseException e) {
                //检查是否是年月格式
                format = new SimpleDateFormat("yyyy-MM");

                try {
                    format.parse(date);
                } catch (ParseException ex) {
                    //检查是否是年格式
                    format = new SimpleDateFormat("yyyy");

                    try {
                        format.parse(date);
                    } catch (ParseException exc) {
                        //都不是，抛出异常
                        throw new ApiException(ResultCode.BAD_REQUEST, "日期格式不合法");
                    }

                }

            }

        }
        //检查orderType是否合法
        Long orderType = postManageDTO.getOrderType();
        if (orderType != null && (orderType < 0 || orderType > 3)) {
            throw new ApiException(ResultCode.BAD_REQUEST,"排序类型不合法");
        }

        //检查status
        Long status = postManageDTO.getStatus();
        if(status != null && (status < 0 || status > 2)){
            throw new ApiException(ResultCode.BAD_REQUEST,"文章状态不合法");
        }
    }

    @Override
    public Page listByManage(PageParamDTO pageParamDTO) {
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());
        Page<UserPost> page = userPostMapper.listByManage(userPostPage);
        return new Page<UserPostManageVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostManageVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public UserPostManageDetailsVO manageDetail(Long postId) {
        UserPost userPost = userPostMapper.getOneForManage(postId);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"查看的文章不存在");
        }
        return this.convertUserPostManageDetailsVO(userPost);
    }

    @Override
    public Page listExcellentByManage(PageParamDTO pageParamDTO){
        Page<ExcellentPost> post = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());
        List<ExcellentPost> records = excellentPostService.listExcellentByManage(post).getRecords();

        //转换为VO对象
        List<ExcellentPostVO> VOPosts = excellentPostService.convertExcellentPostVOs(records);

        return new Page<ExcellentPostVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(VOPosts)
                .setTotal(post.getTotal());
    }

    @Override
    //@GlobalTransactional
    public void controlStatus(Long userId,Long id,Long type){
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("用户不合法");
        }
        ExcellentPost post = excellentPostService.getOneByManage(id);
        if (post == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"牛蛙内容不存在");
        }
        if (type == 0) {
            post.setIsExamine(true);
            post.setIsDelete(false);
            excellentPostService.updateStatus(post);
        } else if (type == 1) {
            post.setIsExamine(true);
            post.setIsDelete(true);
            excellentPostService.updateStatus(post);
        } else if (type == 2) {
            post.setIsExamine(false);
            post.setIsDelete(true);
            excellentPostService.updateStatus(post);
        }else {
            throw new ApiException(ResultCode.BAD_REQUEST,"操作类型不合法");
        }
    }

    /**
     * 检验 UserPostListIdList
     */
    public void checkUserPostListBelongUserId(Long userId, List<UserPost> userPostList) {
        for (UserPost userPost : userPostList) {
            if (!userId.equals(userPost.getAuthorId())) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        }
    }

    private void checkUserPostParamDTO(UserPostParamDTO userPostParamDTO) {
        // 检查 categoryId 是否存在
        if (categoryService.getById(userPostParamDTO.getCategoryId()) == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "分类不存在");
        }
    }

    /**
     * UserPost => UserPostVO
     */
    public List<UserPostVO> convertUserPostVOs(List<UserPost> userPostList) {
        return userPostList.parallelStream()
                .map(this::convertUserPostVO)
                .collect(Collectors.toList());
    }

    public UserPostVO convertUserPostVO(UserPost userPost) {
        // 获取 分类
        Category category = categoryService.getById(userPost.getCategoryId());

        // 构建 UserPostVO
        UserPostVO userPostVO = new UserPostVO();
        BeanUtil.copyProperties(userPost, userPostVO);
        userPostVO.setCategoryName(category.getName());

        return userPostVO;
    }

    /**
     * UserPost => UserPostDetailsVO
     */
    public UserPostDetailsVO convertUserPostDetailsVO(UserPost userPost) {
        // 获取 分类
        Category category = categoryService.getById(userPost.getCategoryId());
        // 获取 作者
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();

        // 获取 SubtaskDTOList
        List<SubtaskDTO> subtaskDTOList = subtaskService.listSubtasksByUserPostId(userPost.getId());

        // 获取 LinkUrlList
        List<UrlDTO> linkUrlList = new ArrayList<>();
        for (SubtaskDTO subtaskDTO : subtaskDTOList) {
            linkUrlList.addAll(subtaskDTO.getLinkUrlList());
        }

        // 构建 UserPostDetailsVO
        UserPostDetailsVO userPostDetailsVO = new UserPostDetailsVO();
        BeanUtil.copyProperties(userPost, userPostDetailsVO);
        userPostDetailsVO.setCategoryName(category.getName());
        userPostDetailsVO.setSubtaskList(subtaskDTOList);
        userPostDetailsVO.setAuthorNickName(user.getNickName());
        userPostDetailsVO.setLinkUrlList(linkUrlList);

        return userPostDetailsVO;
    }

    public List<UserPostManageVO> convertUserPostManageVOs(List<UserPost> userPostList){
        return userPostList.parallelStream()
                .map(this::convertUserPostManageVO)
                .collect(Collectors.toList());
    }

    /**
     * UserPost => UserPostManageVO
     */
    public UserPostManageVO convertUserPostManageVO(UserPost userPost) {
        // 获取 作者
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();
        // 获取 分类
        Category category = categoryService.getById(userPost.getCategoryId());
        // 获取 分区
        Area area = areaService.getById(userPost.getAreaId());

        // 构建 UserPostManageVO
        UserPostManageVO userPostManageVO = new UserPostManageVO();
        BeanUtil.copyProperties(userPost, userPostManageVO);
        userPostManageVO.setCategoryName(category.getName());
        userPostManageVO.setAuthorNickName(user.getNickName());
        userPostManageVO.setAreaName(area.getName());

        //设置状态
        if (!userPost.getCompleted()) {
            userPostManageVO.setStatus(2L);
        }else if(userPost.getIsDelete()){
            //牛蛙经验的帖子需要通过牛蛙内容来判断
            if ("牛蛙经验".equals(area.getName())) {
                if(excellentPostMapper.getOneByPostId(userPost.getId()).getIsDelete()){
                    userPostManageVO.setStatus(0L);
                }else {
                    userPostManageVO.setStatus(1L);
                }
            }else{
                //普通帖子正常设置
                userPostManageVO.setStatus(0L);
            }
        }else{
            userPostManageVO.setStatus(1L);
        }

        //获取收藏量
        Long count = (long) favUserPostService
                .list(new QueryWrapper<FavUserPost>().eq("user_post_id", userPost.getId()))
                .size();
        userPostManageVO.setFavCount(count);

        //获取复用数
        userPostManageVO.setUsedCount(userPost.getTryingCount()+ userPost.getSuccessCount());
        return userPostManageVO;
    }

    /**
     * UserPost => UserPostManageDetailsVO
     */
    public UserPostManageDetailsVO convertUserPostManageDetailsVO(UserPost userPost){
        // 获取 作者
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();
        // 获取 分类
        Category category = categoryService.getById(userPost.getCategoryId());
        // 获取 分区
        Area area = areaService.getById(userPost.getAreaId());

        // 获取 SubtaskDTOList
        List<SubtaskDTO> subtaskDTOList = subtaskService.listSubtasksByUserPostId(userPost.getId());

        // 获取 LinkUrlList
        List<UrlDTO> linkUrlList = new ArrayList<>();
        for (SubtaskDTO subtaskDTO : subtaskDTOList) {
            linkUrlList.addAll(subtaskDTO.getLinkUrlList());
        }

        // 构建 UserPostManageVO
        UserPostManageDetailsVO userPostManageDetailsVO = new UserPostManageDetailsVO();
        BeanUtil.copyProperties(userPost, userPostManageDetailsVO);
        userPostManageDetailsVO.setCategoryName(category.getName());
        userPostManageDetailsVO.setSubtaskList(subtaskDTOList);
        userPostManageDetailsVO.setAreaName(area.getName());
        userPostManageDetailsVO.setAuthorNickName(user.getNickName());
        userPostManageDetailsVO.setLinkUrlList(linkUrlList);


        //设置状态
        if (!userPost.getCompleted()) {
            userPostManageDetailsVO.setStatus(2L);
        }else if(userPost.getIsDelete()){
            //牛蛙经验的帖子需要通过牛蛙内容来判断
            if ("牛蛙经验".equals(area.getName())) {
                if(excellentPostMapper.getOneByPostId(userPost.getId()).getIsDelete()){
                    userPostManageDetailsVO.setStatus(0L);
                }else {
                    userPostManageDetailsVO.setStatus(1L);
                }
            }else{
                //普通帖子正常设置
                userPostManageDetailsVO.setStatus(0L);
            }
        }else{
            userPostManageDetailsVO.setStatus(1L);
        }

        Long count = (long) favUserPostService
                .list(new QueryWrapper<FavUserPost>().eq("user_post_id", userPost.getId()))
                .size();
        userPostManageDetailsVO.setFavCount(count);

        userPostManageDetailsVO.setUsedCount(userPost.getTryingCount()+ userPost.getSuccessCount());
        return userPostManageDetailsVO;
    }
}
