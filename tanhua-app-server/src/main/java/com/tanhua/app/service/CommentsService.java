package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.enums.CommentType;
import com.tanhua.model.mongo.Comment;
import com.tanhua.model.vo.CommentVo;
import com.tanhua.model.vo.MovementsVo;
import com.tanhua.model.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 15:53
 */

@Service
@Slf4j
public class CommentsService {

    @DubboReference
    private CommentApi commentApi;
    @DubboReference
    private UserInfoApi userInfoApi;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveComment(String movementId, String content) {
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(movementId));
        comment.setCommentType(CommentType.COMMENT.getType());
        comment.setContent(content);
        comment.setUserId(UserHolder.getUserId());
        comment.setCreated(System.currentTimeMillis());
        // 保存评论，返回评论的总数量
        Integer commentCount = commentApi.save(comment);
        log.warn("commentcount = {}", commentCount);
    }

    public PageResult findComments(String movementId, Integer page, Integer pagesize) {
        PageResult pr = commentApi.findComments(movementId, CommentType.COMMENT, page, pagesize);
        List<Comment> items = (List<Comment>) pr.getItems();
        // 查询不到返回空
        if(CollUtil.isEmpty(items)){
            return new PageResult();
        }
        List<Long> userIds = CollUtil.getFieldValues(items, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(userIds, null);
        // 构建vo对象
//        List<CommentVo> vos = Collections.emptyList(); // 这个没有添加和删除方法
        List<CommentVo> vos = new ArrayList<>();
        for (Comment comment : items) {
            UserInfo userInfo = infoMap.get(comment.getUserId());
            if(userInfo != null){
                CommentVo vo = CommentVo.init(userInfo, comment);
                vos.add(vo);
            }
        }
        pr.setItems(vos);
        return pr;
    }


}
