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
        // ????????????
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // ??????????????????, ????????????
        if (categoryIdList == null){
            Page<UserPost> page = this.page(userPostPage);
            return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(this.convertUserPostVOs(page.getRecords()))
                    .setTotal(page.getTotal());
        }

        // ??????????????????
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        // ?????? categoryIdList
        for (Long categoryId : categoryIdList) {
            if (categoryId != null) {
                queryWrapper.eq("category_id", categoryId).or();
            }
        }

        // ??????
        Page<UserPost> page = this.page(userPostPage,queryWrapper);
        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listUserPostsByUserId(Long userId, PageParamDTO pageParamDTO) {
        // ????????????
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // ????????????
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<UserPost>()
                .eq("author_id", userId);

        Page<UserPost> page = this.page(userPostPage,queryWrapper);

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO) {
        // ??????
        Page<FavUserPost> favUserPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        QueryWrapper<FavUserPost> favQueryWrapper = new QueryWrapper<FavUserPost>()
                .eq("user_id", userId).orderByDesc("gmt_create");

        Page<FavUserPost> page = favUserPostService.page(favUserPostPage,favQueryWrapper);
        List<FavUserPost> favUserPostList = page.getRecords();

        // ??????????????????????????????
        if (favUserPostList.isEmpty()) {
            return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                    .setRecords(new ArrayList<>())
                    .setTotal(page.getTotal());
        }

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (FavUserPost favUserPost : favUserPostList) {
            queryWrapper.eq("id", favUserPost.getUserPostId()).or();
        }

        // ??????
        List<UserPost> userPostList = new ArrayList<>(this.list(queryWrapper));

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(userPostList))
                .setTotal(page.getTotal());
    }

    @Override
    public Page listUserPostById(PageParamDTO pageParamDTO, Long userId, int orderType) {
        // ??????
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",userId);

        //??????orderType?????????????????????
        if (orderType == 0) {
            queryWrapper.orderByDesc("gmt_create");
        } else if (orderType == 1) {
            queryWrapper.orderByDesc("view_count");
        } else if (orderType == 2) {
            queryWrapper.orderByDesc("trying_count");
        }else {
            throw new ApiException(ResultCode.BAD_REQUEST,"??????????????????");
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

        // ?????? ????????? ????????????
        if (hotUserPostIdList == null || hotUserPostIdList.isEmpty()) {
            return this.convertUserPostVOs(new ArrayList<>());
        }

        // ??????????????????
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (Long userPostId : hotUserPostIdList) {
            if (userPostId != null) {
                queryWrapper.eq("user_post_id", userPostId).or();
            }
        }

        // ??????
        List<UserPost> userPostList = this.list(queryWrapper);

        return this.convertUserPostVOs(userPostList);
    }

    @Override
    public Page listUserPostsByQuery(PageParamDTO pageParamDTO, String words) {
        // ????????????
        Page<UserPost> userPostPage = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        // ??????????????????
        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<UserPost>()
                .like("title", words).or()
                .like("tags", words).or()
                .like("content", words);

        // ??????
        Page<UserPost> page = this.page(userPostPage,queryWrapper);

        return new Page<UserPostVO>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    /**
     * ===========================??????===========================
     */
    @Override
    public UserPostDetailsVO detail(Long id) {
        // ?????? UserPost
        UserPost userPost = this.getById(id);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "???????????????");
        }

        // ????????? + 1
        hotListService.incrUserPost(id);

        return convertUserPostDetailsVO(userPost);
    }

    @Override
    public Boolean publishUserPost(Long userId, UserPostParamDTO userPostParamDTO) {
        // ????????????
        this.checkUserPostParamDTO(userPostParamDTO);

        // ??????????????????
        User user = apiService.getUserById(userId).getBody().getData();

        // ?????? userPostId
        Long userPostId = selfIdGenerator.nextId(null);

        // ?????? subtaskList
        subtaskService.saveSubtasksByUserPostId(userPostId, userPostParamDTO.getSubtaskList());

        // ?????? userPost
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
        // ????????????????????????
        UserPost userPostDB = this.getById(userPostId);
        if (userPostDB == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "????????????????????????");
        }
        // ??????????????????????????? userId
        if (!userPostDB.getAuthorId().equals(userId)) {
            throw new ApiException(ResultCode.FORBIDDEN, "?????????????????????");
        }
        // ???????????????????????????
        this.checkUserPostParamDTO(userPostParamDTO);

        // ?????? subtasks
        subtaskService.updateSubtasksByUserPostId(userPostId, userPostParamDTO.getSubtaskList());

        // ?????? userPost
        UserPost userPost = new UserPost();
        BeanUtil.copyProperties(userPostParamDTO, userPost);
        userPost.setId(userPostId);
        // ?????????????????????
        userPost.setTryingCount(null);
        userPost.setSuccessCount(null);
        userPost.setAuthorId(null);
        userPost.setIsAuthentic(null);

        return this.updateById(userPost);
    }

    /**
     *   ============================????????????================================
     */
    @Override
    public Boolean removeUserPosts(Long userId, List<Long> userPostIdList) {
        // ??????
        if (userPostIdList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST, "???????????????????????????");
        }

        QueryWrapper<UserPost> queryWrapper = new QueryWrapper<>();
        for (Long userPostId : userPostIdList) {
            queryWrapper.eq("id", userPostId).or();
        }

        //???????????????????????????
        RoleAdminRelation admin = apiService.getOne(userId).getBody().getData();

        // ?????????????????????????????? userId???
        List<UserPost> userPostList = this.list(queryWrapper);
        for (UserPost userPost : userPostList) {
            if (!userId.equals(userPost.getAuthorId()) && admin == null) {
                throw new ApiException(ResultCode.FORBIDDEN, "?????????????????????");
            }
        }

        // ?????? SubtaskList
        if (admin == null) {
            subtaskService.removeSubtasksByUserPostId(userPostIdList);
        }

        return this.removeByIds(userPostIdList);
    }

    @Override
    //@GlobalTransactional
    public void onlineUserPost(Long userId,Long userPostId){
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("???????????????");
        }
        UserPost userPost = userPostMapper.getOneForManage(userPostId);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"???????????????????????????");
        }
        Area area = areaService.getById(userPost.getAreaId());

        //???????????????????????????
        if ("????????????".equals(area.getName())) {
            ExcellentPost excellentPost = excellentPostMapper.getOneByPostId(userPostId);
            if (excellentPost == null) {
                throw new ApiException(ResultCode.BAD_REQUEST,"???????????????????????????");
            }
            controlStatus(userId,excellentPost.getId(),0L);
        }else{
            Integer res = userPostMapper.onlineUserPost(userPostId);
            if (res == 0) {
                throw new ApiException(ResultCode.BAD_REQUEST,"??????????????????");
            }
        }
    }

    @Override
    //@GlobalTransactional
    public void removeUserPostForManage(Long userId, Long userPostId) {
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("???????????????");
        }

        UserPost userPost = userPostMapper.getOneForManage(userPostId);
        if (userPost == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"???????????????????????????");
        }
        Area area = areaService.getById(userPost.getAreaId());

        if ("????????????".equals(area.getName())) {
            ExcellentPost excellentPost = excellentPostMapper.getOneByPostId(userPostId);
            if (excellentPost == null) {
                throw new ApiException(ResultCode.BAD_REQUEST,"???????????????????????????");
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
            throw new ApiException("???????????????");
        }

        Boolean res = userPostMapper.deleteUserPostForManage(userPostId);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST,"???????????????????????????");
        }
    }

    @Override
    public Page listBySearch(PageParamDTO pageParamDTO, PostManageDTO postManageDTO) {
        Long zone = postManageDTO.getZone();
        Page<UserPost> page = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());

        this.checkPostManageDTO(postManageDTO);

        if (zone == null) {
            //??????????????????
            page = userPostMapper.searchAllPost(page, postManageDTO);
        } else if (zone == 0) {
            //?????????????????????
            Area area1 = areaService.getOne(new QueryWrapper<Area>().eq("name","????????????"));
            page = userPostMapper.searchCommonPost(page, postManageDTO, area1.getId());
        } else if (zone == 1) {
            //???????????????????????????
            page = excellentPostMapper.searchExcellentPost(page, postManageDTO);
        }else{
            throw new ApiException(ResultCode.BAD_REQUEST,"???????????????");
        }

        return new Page<UserPostManageVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(this.convertUserPostManageVOs(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void checkPostManageDTO(PostManageDTO postManageDTO) {
        //????????????????????????
        String date = postManageDTO.getDate();
        if (!"".equals(date)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                //??????????????????????????????
                format.parse(date);
            } catch (ParseException e) {
                //???????????????????????????
                format = new SimpleDateFormat("yyyy-MM");

                try {
                    format.parse(date);
                } catch (ParseException ex) {
                    //????????????????????????
                    format = new SimpleDateFormat("yyyy");

                    try {
                        format.parse(date);
                    } catch (ParseException exc) {
                        //????????????????????????
                        throw new ApiException(ResultCode.BAD_REQUEST, "?????????????????????");
                    }

                }

            }

        }
        //??????orderType????????????
        Long orderType = postManageDTO.getOrderType();
        if (orderType != null && (orderType < 0 || orderType > 3)) {
            throw new ApiException(ResultCode.BAD_REQUEST,"?????????????????????");
        }

        //??????status
        Long status = postManageDTO.getStatus();
        if(status != null && (status < 0 || status > 2)){
            throw new ApiException(ResultCode.BAD_REQUEST,"?????????????????????");
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
            throw new ApiException(ResultCode.BAD_REQUEST,"????????????????????????");
        }
        return this.convertUserPostManageDetailsVO(userPost);
    }

    @Override
    public Page listExcellentByManage(PageParamDTO pageParamDTO){
        Page<ExcellentPost> post = new Page<>(pageParamDTO.getPageNum(), pageParamDTO.getPageSize());
        List<ExcellentPost> records = excellentPostService.listExcellentByManage(post).getRecords();

        //?????????VO??????
        List<ExcellentPostVO> VOPosts = excellentPostService.convertExcellentPostVOs(records);

        return new Page<ExcellentPostVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(VOPosts)
                .setTotal(post.getTotal());
    }

    @Override
    //@GlobalTransactional
    public void controlStatus(Long userId,Long id,Long type){
        if (!apiService.checkOperator(userId).getBody().getData()) {
            throw new ApiException("???????????????");
        }
        ExcellentPost post = excellentPostService.getOneByManage(id);
        if (post == null) {
            throw new ApiException(ResultCode.BAD_REQUEST,"?????????????????????");
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
            throw new ApiException(ResultCode.BAD_REQUEST,"?????????????????????");
        }
    }

    /**
     * ?????? UserPostListIdList
     */
    public void checkUserPostListBelongUserId(Long userId, List<UserPost> userPostList) {
        for (UserPost userPost : userPostList) {
            if (!userId.equals(userPost.getAuthorId())) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        }
    }

    private void checkUserPostParamDTO(UserPostParamDTO userPostParamDTO) {
        // ?????? categoryId ????????????
        if (categoryService.getById(userPostParamDTO.getCategoryId()) == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "???????????????");
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
        // ?????? ??????
        Category category = categoryService.getById(userPost.getCategoryId());

        // ?????? UserPostVO
        UserPostVO userPostVO = new UserPostVO();
        BeanUtil.copyProperties(userPost, userPostVO);
        userPostVO.setCategoryName(category.getName());

        return userPostVO;
    }

    /**
     * UserPost => UserPostDetailsVO
     */
    public UserPostDetailsVO convertUserPostDetailsVO(UserPost userPost) {
        // ?????? ??????
        Category category = categoryService.getById(userPost.getCategoryId());
        // ?????? ??????
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();

        // ?????? SubtaskDTOList
        List<SubtaskDTO> subtaskDTOList = subtaskService.listSubtasksByUserPostId(userPost.getId());

        // ?????? LinkUrlList
        List<UrlDTO> linkUrlList = new ArrayList<>();
        for (SubtaskDTO subtaskDTO : subtaskDTOList) {
            linkUrlList.addAll(subtaskDTO.getLinkUrlList());
        }

        // ?????? UserPostDetailsVO
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
        // ?????? ??????
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();
        // ?????? ??????
        Category category = categoryService.getById(userPost.getCategoryId());
        // ?????? ??????
        Area area = areaService.getById(userPost.getAreaId());

        // ?????? UserPostManageVO
        UserPostManageVO userPostManageVO = new UserPostManageVO();
        BeanUtil.copyProperties(userPost, userPostManageVO);
        userPostManageVO.setCategoryName(category.getName());
        userPostManageVO.setAuthorNickName(user.getNickName());
        userPostManageVO.setAreaName(area.getName());

        //????????????
        if (!userPost.getCompleted()) {
            userPostManageVO.setStatus(2L);
        }else if(userPost.getIsDelete()){
            //??????????????????????????????????????????????????????
            if ("????????????".equals(area.getName())) {
                if(excellentPostMapper.getOneByPostId(userPost.getId()).getIsDelete()){
                    userPostManageVO.setStatus(0L);
                }else {
                    userPostManageVO.setStatus(1L);
                }
            }else{
                //????????????????????????
                userPostManageVO.setStatus(0L);
            }
        }else{
            userPostManageVO.setStatus(1L);
        }

        //???????????????
        Long count = (long) favUserPostService
                .list(new QueryWrapper<FavUserPost>().eq("user_post_id", userPost.getId()))
                .size();
        userPostManageVO.setFavCount(count);

        //???????????????
        userPostManageVO.setUsedCount(userPost.getTryingCount()+ userPost.getSuccessCount());
        return userPostManageVO;
    }

    /**
     * UserPost => UserPostManageDetailsVO
     */
    public UserPostManageDetailsVO convertUserPostManageDetailsVO(UserPost userPost){
        // ?????? ??????
        User user = apiService.getUserById(userPost.getAuthorId()).getBody().getData();
        // ?????? ??????
        Category category = categoryService.getById(userPost.getCategoryId());
        // ?????? ??????
        Area area = areaService.getById(userPost.getAreaId());

        // ?????? SubtaskDTOList
        List<SubtaskDTO> subtaskDTOList = subtaskService.listSubtasksByUserPostId(userPost.getId());

        // ?????? LinkUrlList
        List<UrlDTO> linkUrlList = new ArrayList<>();
        for (SubtaskDTO subtaskDTO : subtaskDTOList) {
            linkUrlList.addAll(subtaskDTO.getLinkUrlList());
        }

        // ?????? UserPostManageVO
        UserPostManageDetailsVO userPostManageDetailsVO = new UserPostManageDetailsVO();
        BeanUtil.copyProperties(userPost, userPostManageDetailsVO);
        userPostManageDetailsVO.setCategoryName(category.getName());
        userPostManageDetailsVO.setSubtaskList(subtaskDTOList);
        userPostManageDetailsVO.setAreaName(area.getName());
        userPostManageDetailsVO.setAuthorNickName(user.getNickName());
        userPostManageDetailsVO.setLinkUrlList(linkUrlList);


        //????????????
        if (!userPost.getCompleted()) {
            userPostManageDetailsVO.setStatus(2L);
        }else if(userPost.getIsDelete()){
            //??????????????????????????????????????????????????????
            if ("????????????".equals(area.getName())) {
                if(excellentPostMapper.getOneByPostId(userPost.getId()).getIsDelete()){
                    userPostManageDetailsVO.setStatus(0L);
                }else {
                    userPostManageDetailsVO.setStatus(1L);
                }
            }else{
                //????????????????????????
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
