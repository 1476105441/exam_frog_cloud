package com.wjs.examfrog.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.examfrog.common.ResultCode;
import com.wjs.examfrog.component.SelfIdGenerator;
import com.wjs.examfrog.dto.*;
import com.wjs.examfrog.entity.*;
import com.wjs.examfrog.exception.ApiException;
import com.wjs.examfrog.user.mapper.UserMapper;
import com.wjs.examfrog.user.service.*;
import com.wjs.examfrog.user.util.JwtTokenUtil;
import com.wjs.examfrog.vo.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${RBAC.defaultRoleId:2}")
    private Long defaultRoleId;
    @Resource
    private SelfIdGenerator selfIdGenerator;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private UserCacheServiceImpl userCacheService;
    @Resource
    private ApiService apiService;
    @Resource
    private RoleUserRelationService roleUserRelationService;
    @Resource
    private PlanningFolderService planningFolderService;
    @Resource
    private PlanningService planningService;
    @Resource
    private LikeUserPostService likeUserPostService;
    @Resource
    private FollowUserService followUserService;
    @Resource
    private FavPostFolderService favPostFolderService;
    @Resource
    private AdminService adminService;
    @Resource
    private UserMapper userMapper;

    @Override
    public void insert(LoginParamDTO loginParamDTO) {
        Long userId = selfIdGenerator.nextId(null);

        // ?????? ?????? ??????
        RoleUserRelation roleUserRelation = new RoleUserRelation();
        roleUserRelation.setUserId(userId);
        roleUserRelation.setRoleId(defaultRoleId);
        roleUserRelationService.save(roleUserRelation);

        // ?????? user
        User user = new User();
        BeanUtils.copyProperties(loginParamDTO, user);
        user.setId(userId);
        user.setStatus(0);
        user.setFansCount(0L);
        user.setFollowCount(0L);
        user.setSignature("??????????????????????????????");
        user.setLoginTime(LocalDateTime.now());
        user.setFreeze(false);

        if(userMapper.insert(user) == 0)
            throw new ApiException("????????????");
    }

    /**
     * ??????
     */
    @Override
    //@GlobalTransactional
    public UserLoginVO register(LoginParamDTO loginParamDTO) {
        // ?????? openId ???????????? 32 ???
        if (loginParamDTO.getOpenId().length() != 32) {
            throw new ApiException(ResultCode.BAD_REQUEST, "openid?????????");
        }

        // ???????????????????????????????????????, ??????????????????
        User userDB = getUserByOpenId(loginParamDTO.getOpenId());
        if (userDB != null) {
            return this.login(loginParamDTO.getOpenId());
        }

        // ?????? id
        Long userId = selfIdGenerator.nextId(null);

        // ?????? ?????? ??????
        RoleUserRelation roleUserRelation = new RoleUserRelation();
        roleUserRelation.setUserId(userId);
        roleUserRelation.setRoleId(defaultRoleId);
        roleUserRelationService.save(roleUserRelation);

        // ?????? user
        User user = new User();
        BeanUtils.copyProperties(loginParamDTO, user);
        user.setId(userId);
        user.setStatus(0);
        user.setFansCount(0L);
        user.setFollowCount(0L);
        user.setSignature("??????????????????????????????");
        user.setLoginTime(LocalDateTime.now());
        user.setFreeze(false);
//        // ??? openId ??????????????????
//        String encodePassword = passwordEncoder.encode(user.getOpenId());
//        user.setOpenId(encodePassword);
        if(userMapper.insert(user) == 0)
            throw new ApiException("????????????");

        return this.login(user.getOpenId());
    }

    /**
     * ??????
     */
    @Override
    public UserLoginVO login(String openId) {
        // SecurityContextHolder ??????????????????
        User user = this.getUserByOpenId(openId);

        // ?????????????????? token, userInfoDTO
        // ??????????????? admin ????????? token
        String token = jwtTokenUtil.generateToken(user, false);

        // ?????? UserDetailsVO
        UserDetailsVO userDetailsVO = convertUserDetailsVO(user);

        return new UserLoginVO(token, userDetailsVO);
    }

    @Override
    public UserDetailsVO detail(Long id) {
        // ?????? User
        User user = this.getById(id);
        if (user == null) {
            throw new ApiException(ResultCode.BAD_REQUEST, "???????????????");
        }

        return this.convertUserDetailsVO(user);
    }

    /**
     * ===============================FollowUser=============================
     */
    @Override
    public Page listFollows(Long userId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return followUserService.listFollows(userId, pageParamDTO);
    }

    @Override
    public Page listFans(Long userId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return followUserService.listFans(userId, pageParamDTO);
    }

    @Override
    public void saveFollowUser(Long userId, Long followId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        followUserService.saveFollowUser(userId, followId);
    }

    @Override
    public void removeFollowUsers(Long userId, List<Long> followIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        followUserService.removeFollowUsers(userId, followIdList);
    }

    /**
     * ========================UserPost=========================
     */


    @Override
    public void saveLikeUserPost(Long userId, Long userPostId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        likeUserPostService.saveLikeUserPost(userId, userPostId);
    }

    @Override
    public void removeLikeUserPost(Long userId, Long userPostId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        likeUserPostService.removeLikeUserPost(userId, userPostId);
    }



    /**
     * ========================FavUserPost=========================
     */
    /**
     * ????????????????????? ????????? UserPostVO
     */
    @Override
    public Page listFavUserPosts(Long userId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return apiService.listFavUserPosts(userId, pageParamDTO).getBody().getData();
    }

    /**
     * ?????? UserPost
     */
    @Override
    @GlobalTransactional
    public void publishUserPost(Long userId, UserPostParamDTO userPostParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        if(!apiService.publishUserPost(userId, userPostParamDTO).getBody().getData())
            throw new ApiException("????????????????????????");
    }

    /**
     * ?????? UserPost
     */
    @Override
    @GlobalTransactional
    public void updateUserPost(Long userId, Long userPostId, UserPostParamDTO userPostParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        if (!apiService.updateUserPost(userId, userPostId, userPostParamDTO).getBody().getData()) {
            throw new ApiException("????????????????????????");
        }
    }

    /**
     * ?????? UserPost
     */
    @Override
    @GlobalTransactional
    public void removeUserPosts(Long userId, List<Long> userPostIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        if (!apiService.removeUserPosts(userId, userPostIdList).getBody().getData()) {
            throw new ApiException("????????????????????????");
        }
    }

    /**
     * ?????? UserPost
     */
    @Override
    @GlobalTransactional
    public void saveFavUserPost(Long userId, Long userPostId, Long favPostFolderId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        if(!apiService.saveFavUserPost(userId, userPostId, favPostFolderId).getBody().getData())
            throw new ApiException("????????????");
    }


    /**
     * ???????????? UserPost
     */
    @Override
    @GlobalTransactional
    public void removeFavUserPosts(Long userId, List<Long> userPostIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        if(!apiService.removeFavUserPosts(userId, userPostIdList).getBody().getData())
            throw new ApiException("????????????");
    }

    /**
     * ==============================Subtask============================
     */
    /**
     * ?????? Subtask
     */
    @Override
    public void bingo(Long userId, Long planningId, Long subtaskId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningService.bingo(userId, planningId, subtaskId);
    }

    @Override
    public void cancelBingo(Long userId, Long planningId, Long subtaskId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningService.cancelBingo(userId, planningId, subtaskId);
    }

    /**
     * =======================PlanningFolder======================
     */
    @Override
    public Page listPlanningFolders(Long userId, Long planningFolderId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return planningFolderService.listPlanningFolders(userId, planningFolderId, pageParamDTO);
    }

    @Override
    public void savePlanningFolder(Long userId, String planningFolderName, Long pid) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.savePlanningFolder(userId, planningFolderName, pid);
    }

    @Override
    public void updatePlanningFolder(Long userId, Long planningFolderId, String planningFolderName) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.updatePlanningFolder(userId, planningFolderId, planningFolderName);
    }

    @Override
    public void removePlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.removePlanningFolders(userId, planningFolderIdList);
    }

    @Override
    public void movePlanningFolders(Long userId, List<Long> planningFolderIdList, Long targetPlanningFolderId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.movePlanningFolders(userId, planningFolderIdList, targetPlanningFolderId);
    }

    @Override
    public PlanningDetailsVO getPlanningDetail(Long userId, Long planningFolderId, Long planningId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return planningFolderService.getPlanningDetail(userId, planningFolderId, planningId);
    }

//    ============================Planning=============================

    @Override
    public Page listPlannings(Long userId, Long planningFolderId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return planningFolderService.listPlannings(userId, planningFolderId, pageParamDTO);
    }

    @Override
    public void savePlanning(Long userId, Long planningFolderId, PlanningParamDTO planningParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.savePlanning(userId, planningFolderId, planningParamDTO);
    }

    @Override
    public void updatePlanning(Long userId, Long planningFolderId, Long planningId, PlanningParamDTO planningParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.updatePlanning(userId, planningFolderId, planningId, planningParamDTO);
    }

    @Override
    public void removePlannings(Long userId, Long planningFolderId, List<Long> planningList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.removePlannings(userId, planningFolderId, planningList);
    }

    @Override
    public void movePlannings(Long userId, Long planningFolderId, List<Long> planningIdList, Long targetPlanningFolderId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.movePlannings(userId, planningFolderId, planningIdList, targetPlanningFolderId);
    }


    /**
     * =============================?????????=============================
     */

    @Override
    public Page listDeletedPlanningFolders(Long userId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return planningFolderService.listDeletedPlanningFolders(userId, pageParamDTO);
    }

    @Override
    public void recoveryDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.recoveryDeletedPlanningFolders(userId, planningFolderIdList);
    }

    @Override
    public void removeDeletedPlanningFolders(Long userId, List<Long> planningFolderIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.removeDeletedPlanningFolders(userId, planningFolderIdList);
    }

    @Override
    public Page listDeletedPlannings(Long userId, PageParamDTO pageParamDTO) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        return planningService.listDeletedPlannings(userId, pageParamDTO);
    }

    @Override
    public void recoveryDeletedPlannings(Long userId, List<Long> planningIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningService.recoveryDeletedPlanningsByPlanningIdList(userId, planningIdList);
    }

    @Override
    public void removeDeletedUserPosts(Long userId, List<Long> planningIdList) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningService.removeDeletedPlanningsByPlanningIdList(userId, planningIdList);
    }

    @Override
    public void removeAllDeleted(Long userId) {
        // ??????????????????????????? userId
        this.checkOperator(userId);

        planningFolderService.removeAllDeletedPlanningFolders(userId);
        planningService.removeAllDeletedPlanning(userId);
    }

    /**
     * ?????? openId ????????????
     */
    @Override
    public User getUserByOpenId(String openId) {
        // ?????? redis ???????????????
        User user = userCacheService.getUserByOpenId(openId);
        if (user != null) {
            return user;
        }

        // redis ?????????, ?????????????????????
        user = this.getOne(new QueryWrapper<User>().eq("open_id", openId));

        // ?????? redis ???
        if (user != null) {
            userCacheService.setUserByOpenId(user);
        }
        return user;
    }

    public UserVO convertUserVO(User user) {
        // ?????? UserDetailsVO
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);

        return userVO;
    }

    @Override
    public List<UserVO> convertUserVOList(List<User> userList) {
        return userList.parallelStream()
                .map(this::convertUserVO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveFavPostFolder(Long userId, FavPostFolder favPostFolder) {
        this.checkOperator(userId);
        favPostFolderService.saveFavPostFolder(userId, favPostFolder);
    }

    @Override
    public Page listFavPostFolders(Long userId, Long favPostFolderId, Long visitedId, PageParamDTO pageParamDTO) {
        this.checkOperator(userId);
        return favPostFolderService.listFavPostFolders(userId, favPostFolderId, visitedId, pageParamDTO);
    }

    @Override
    public void updateFavPostFolder(Long userId, FavPostFolder favPostFolder, Long favPostFolderId) {
        this.checkOperator(userId);
        favPostFolderService.updateFavPostFolder(userId, favPostFolder, favPostFolderId);
    }

    @Override
    public void moveFavPostFolder(Long userId, List<Long> favPostFolderIdList, Long targetId) {
        this.checkOperator(userId);
        favPostFolderService.moveFavPostFolder(userId, favPostFolderIdList, targetId);
    }

    @Override
    public void removeFavPostFolder(Long userId, List<Long> favPostFolderIdList) {
        this.checkOperator(userId);
        favPostFolderService.removeFavPostFolder(userId, favPostFolderIdList);
    }

    @Override
    public Page<UserVO> listForManage(PageParamDTO pageParamDTO) {
        Page<User> page = new Page<User>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize());
        page = this.page(page);
        return new Page<UserVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(this.convertUserVOList(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public Page<UserVO> listBySearch(PageParamDTO pageParamDTO, UserParamDTO userParamDTO) {
        Page<User> page = new Page<User>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (userParamDTO.getId() != null) {
            queryWrapper.eq("id",userParamDTO.getId());
        }
        if (userParamDTO.getNickName() != null && !"".equals(userParamDTO.getNickName())) {
            queryWrapper.eq("nick_name",userParamDTO.getNickName());
        }
        if (userParamDTO.getFreeze() != null) {
            queryWrapper.eq("freeze",userParamDTO.getFreeze());
        }
        if (userParamDTO.getPhone() != null) {
            queryWrapper.eq("phone",userParamDTO.getPhone());
        }
        if (userParamDTO.getLoginTime() != null) {
            if (userParamDTO.getLoginTime() == 0) {
                queryWrapper.gt("login_time", LocalDateTime.now().minusDays(7));
            } else if (userParamDTO.getLoginTime() == 1) {
                queryWrapper.gt("login_time", LocalDateTime.now().minusMonths(1));
            } else if (userParamDTO.getLoginTime() == 2) {
                queryWrapper.gt("login_time", LocalDateTime.now().minusYears(1));
            }
        }

        page = this.page(page,queryWrapper);
        return new Page<UserVO>(pageParamDTO.getPageNum(),pageParamDTO.getPageSize())
                .setRecords(this.convertUserVOList(page.getRecords()))
                .setTotal(page.getTotal());
    }

    @Override
    public void freezeUser(List<Long> idList, Long type) {
        if (idList.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST,"??????id????????????");
        }

        List<User> list = this.listByIds(idList);
        if (list.isEmpty()) {
            throw new ApiException(ResultCode.BAD_REQUEST,"??????/????????????????????????");
        }
        if (type == 0) {
            for (User user : list) {
                user.setFreeze(true);
            }
        } else if (type == 1) {
            for (User user : list) {
                user.setFreeze(false);
            }
        }else {
            throw new ApiException(ResultCode.BAD_REQUEST,"type???????????????");
        }

        boolean res = this.updateBatchById(list);
        if (!res) {
            throw new ApiException(ResultCode.BAD_REQUEST,"??????/????????????");
        }
    }

    /**
     * User => UserDetailsVO
     */
    public UserDetailsVO convertUserDetailsVO(User user) {
        // ?????????, ?????????
        List<UserPost> userPostList = apiService.listUserPost(user.getId()).getBody().getData();
        Long sumLikeCount = 0L, sumFavCount = 0L;
        for (UserPost userPost : userPostList) {
            sumLikeCount += userPost.getLikeCount();
            sumFavCount += userPost.getFavCount();
        }

        // ?????? UserDetailsVO
        UserDetailsVO userDetailsVO = new UserDetailsVO();
        BeanUtil.copyProperties(user, userDetailsVO);
        userDetailsVO.setLikeCount(sumLikeCount);
        userDetailsVO.setFavCount(sumFavCount);
        userDetailsVO.setPostCount((long) userPostList.size());

        return userDetailsVO;
    }

    /**
     * wx??? ?????? => ???????????????????????????
     */
    public Boolean checkOperator(Long userId) {
        User user = this.getById(userId);

        return user == null;
    }

    @Override
    public UserLoginVO adminLogin(AdminDTO adminDTO) {
        Admin adminDB = adminService.getOne(new QueryWrapper<Admin>().eq("username", adminDTO.getUsername()));
        if (!adminDB.getPassword().equals(adminDTO.getPassword())) {
            throw new ApiException(ResultCode.UNAUTHORIZED);
        }
        String openId = adminDB.getOpenId();
        return this.login(openId);
    }

}
