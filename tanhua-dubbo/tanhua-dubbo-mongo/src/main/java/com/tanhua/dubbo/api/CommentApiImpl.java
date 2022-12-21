package com.tanhua.dubbo.api;

import com.tanhua.model.enums.CommentType;
import com.tanhua.model.mongo.Comment;
import com.tanhua.model.mongo.Movement;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 15:58
 */
@DubboService
public class CommentApiImpl implements CommentApi{

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Integer delete(Comment comment) {
        Query query =  Query.query(Criteria.where("publishId").is(comment.getPublishId())
                .and("userId").is(comment.getUserId())
                .and("commentType").is(comment.getCommentType()));
        // 删除comment
        mongoTemplate.remove(query, Comment.class);
        // 更新动态的相关信息
        Query movementQuery = Query.query(Criteria.where("id").is(comment.getPublishId()));
        Update update = new Update();
        if(comment.getCommentType() == CommentType.LIKE.getType()){
            // 喜欢
            update.inc("likeCount", -1);
        } else if(comment.getCommentType() == CommentType.COMMENT.getType()){
            // 评论
            update.inc("commentCount", -1);
        } else{
            // 点赞
            update.inc("loveCount", -1);
        }
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        Movement modify = mongoTemplate.findAndModify(movementQuery, update, options, Movement.class);

        return modify.statisCount(comment.getCommentType());

    }

    @Override
    public boolean hasComment(String movementId, Long userId, CommentType like) {
        Query query =  Query.query(Criteria.where("publishId").is(new ObjectId(movementId))
                .and("userId").is(userId)
                .and("commentType").is(like.getType()));
        // 判断是否存在评论信息
        return mongoTemplate.exists(query, Comment.class);
    }

    @Override
    public Integer save(Comment comment) {
        Movement movement = mongoTemplate.findById(comment.getPublishId(), Movement.class);
        if(movement != null){
            // 设置动态发布人id
            comment.setPublishUserId(movement.getUserId());
        }
        // 保存comment
        mongoTemplate.save(comment);
        // 更新其他数据
        Query query = Query.query(Criteria.where("id").is(comment.getPublishId()));
        Update update = new Update();
        if(comment.getCommentType() == CommentType.LIKE.getType()){
            // 喜欢
            update.inc("likeCount", 1);
        } else if(comment.getCommentType() == CommentType.COMMENT.getType()){
            // 评论
            update.inc("commentCount", 1);
        } else{
            // 点赞
            update.inc("loveCount", 1);
        }
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        Movement modify = mongoTemplate.findAndModify(query, update, options, Movement.class);

        return modify.statisCount(comment.getCommentType());
    }

    @Override
    public List<Comment> findComments(String movementId, CommentType comment, Integer page, Integer pagesize) {
        Criteria criteria = Criteria.where("publishId").is(new ObjectId(movementId))
                .and("commentType").is(comment.getType());
        Query query = Query.query(criteria).skip((page - 1) * pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));

        return mongoTemplate.find(query, Comment.class);
    }




}
