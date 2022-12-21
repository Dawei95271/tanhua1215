package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Friend;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 19:42
 */
@DubboService
public class FriendApiImpl implements FriendApi{

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Friend> findByUserId(Long userId, Integer page, Integer pagesize) {
        Query query =  Query.query(Criteria.where("userId").is(userId))
                .skip((page - 1) * pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));

        return mongoTemplate.find(query, Friend.class);
    }

    @Override
    public void save(Long userId, Long friendId) {
        // 判断好友关系受否存在，不存在则保存
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("friendId").is(friendId));
        if(! mongoTemplate.exists(query, Friend.class)){
            // 不存在，则添加
            Friend friend = new Friend();
            friend.setUserId(userId);
            friend.setFriendId(friendId);
            friend.setCreated(System.currentTimeMillis());
            mongoTemplate.save(friend);
        }

        Query query1 = Query.query(Criteria.where("userId").is(friendId)
                .and("friendId").is(userId));
        if(! mongoTemplate.exists(query1, Friend.class)){
            // 不存在，则添加
            Friend friend = new Friend();
            friend.setUserId(friendId);
            friend.setFriendId(userId);
            friend.setCreated(System.currentTimeMillis());
            mongoTemplate.save(friend);
        }
    }
}
