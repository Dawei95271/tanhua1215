package com.tanhua.dubbo.service;

import com.tanhua.model.mongo.Friend;
import com.tanhua.model.mongo.MovementTimeLine;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/20 17:46
 */
@Component
public class TimeLineService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 开启新线程
    @Async
    public void saveTimeLine(Long userId, ObjectId movementId){
        // 查询好友数据，保存时间线信息
        Query query = Query.query(Criteria.where("userId").is(userId));
        List<Friend> friendList = mongoTemplate.find(query, Friend.class);
        for (Friend friend : friendList) {
            MovementTimeLine line = new MovementTimeLine();
            line.setMovementId(movementId);
            line.setUserId(userId);
            line.setFriendId(friend.getFriendId());
            line.setCreated(System.currentTimeMillis());
            mongoTemplate.save(line);
        }
    }
}
