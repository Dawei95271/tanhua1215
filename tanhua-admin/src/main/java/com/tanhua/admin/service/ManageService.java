package com.tanhua.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.enums.CommentType;
import com.tanhua.model.mongo.Comment;
import com.tanhua.model.mongo.Movement;
import com.tanhua.model.vo.CommentVo;
import com.tanhua.model.vo.MovementsVo;
import com.tanhua.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/23 18:22
 */
@Service
public class ManageService {

    @DubboReference
    private UserInfoApi userInfoApi;
    @DubboReference
    private VideoApi videoApi;
    @DubboReference
    private MovementApi movementApi;
    @DubboReference
    private CommentApi commentApi;

    // 查询用户基本信息
    public UserInfo getUser(Long userId) {
        return userInfoApi.findById(userId);
    }

    // 分页查询用户
    public PageResult findAll(Integer page, Integer pagesize) {
        IPage<UserInfo> iPage = userInfoApi.findAllByPage(page, pagesize);
        return new PageResult(page, pagesize, iPage.getTotal(), iPage.getRecords());
    }

    // 查询uid用户视屏列表
    public PageResult getVideos(Long uid, Integer page, Integer pagesize) {
        PageResult pageResult = videoApi.queryVideosByUid(uid, page, pagesize);
        return pageResult;
    }

    // 分页查询动态
    public PageResult getMovements(Long uid, Integer state, Integer page, Integer pagesize) {

        PageResult pr = movementApi.findMovementByUidAndState(uid, state, page, pagesize);
        List<Movement> items = (List<Movement>) pr.getItems();
        // 判空
        if(CollUtil.isEmpty(items)){
            return  pr;
        }
        // 查询userInfo
        List<Long> ids = CollUtil.getFieldValues(items, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, null);
        List<MovementsVo> vos = new ArrayList<>();
        // 构造vo
        for (Movement item : items) {
            UserInfo info = infoMap.get(item.getUserId());
            if(info != null){
                MovementsVo vo = MovementsVo.init(info, item);
                vos.add(vo);
            }
        }
        pr.setItems(vos);
        return pr;
    }


    // 获取动态详情
    public MovementsVo getMovement(String id) {
        Movement movement = movementApi.findById(id);
        UserInfo info = userInfoApi.findById(movement.getUserId());

        return MovementsVo.init(info, movement);
    }

    // 获取评论列表
    public PageResult getComments(String messageID, Integer page, Integer pagesize) {
        PageResult pr = commentApi.findComments(messageID, CommentType.COMMENT, page, pagesize);
        List<Comment> items = (List<Comment>) pr.getItems();
        if(CollUtil.isEmpty(items)){
            return new PageResult();
        }
        List<Long> ids = CollUtil.getFieldValues(items, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, null);
        List<CommentVo> vos = new ArrayList<>();
        for (Comment item : items) {
            UserInfo userInfo = infoMap.get(item.getUserId());
            if(userInfo != null){
                CommentVo vo = CommentVo.init(userInfo, item);
                vos.add(vo);
            }
        }
        pr.setItems(vos);
        return pr;
    }







}
