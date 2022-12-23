package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.model.mongo.UserLike;
import com.tanhua.model.mongo.UserLocation;
import javafx.scene.effect.Light;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.sound.midi.SysexMessage;
import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/22 14:45
 */
@DubboService
public class UserLikeApiImpl implements UserLikeApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Long> queryNear(Long userId, Double distance) {
        Query query = Query.query(Criteria.where("userId").is(userId));
        UserLocation userLocation = mongoTemplate.findOne(query, UserLocation.class);
        if(userLocation == null){
            return null;
        }
        GeoJsonPoint point = userLocation.getLocation();
        Distance distance1 = new Distance(distance / 1000, Metrics.KILOMETERS);
        Circle circle = new Circle(point, distance1);
        // 查询条件
        Query locationQuery = Query.query(Criteria.where("location").withinSphere(circle));
        List<UserLocation> userLocations = mongoTemplate.find(locationQuery, UserLocation.class);

        return CollUtil.getFieldValues(userLocations, "userId", Long.class);
    }

    @Override
    public Boolean saveOrUpdate(Long userId, Long likeUserId, boolean isLike) {
        try {
            Query query = Query.query(Criteria.where("userId").is(userId)
                    .and("likeUserId").is(likeUserId));
            if(mongoTemplate.exists(query, UserLike.class)){
                // 更新
                Update update = Update.update("isLike", isLike)
                        .set("created", System.currentTimeMillis());
                mongoTemplate.updateFirst(query, update, UserLike.class);
            } else{
                // 保存
                UserLike userLike = new UserLike();
                userLike.setUserId(userId);
                userLike.setLikeUserId(likeUserId);
                userLike.setUpdated(System.currentTimeMillis());
                userLike.setCreated(System.currentTimeMillis());
                userLike.setIsLike(true);
                mongoTemplate.save(userLike);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
