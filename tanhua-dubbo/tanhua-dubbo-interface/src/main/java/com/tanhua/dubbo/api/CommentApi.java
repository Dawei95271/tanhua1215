package com.tanhua.dubbo.api;

import com.tanhua.model.enums.CommentType;
import com.tanhua.model.mongo.Comment;

import java.util.List;

public interface CommentApi {

    // 根据动态id、评论类型,查询评论信息
    List<Comment> findComments(String movementId, CommentType comment, Integer page, Integer pagesize);

    // 保存评论，返回评论总数量
    Integer save(Comment comment);

    // 是否评论动态
    boolean hasComment(String movementId, Long userId, CommentType like);

    // 删除评论
    Integer delete(Comment comment);
}
