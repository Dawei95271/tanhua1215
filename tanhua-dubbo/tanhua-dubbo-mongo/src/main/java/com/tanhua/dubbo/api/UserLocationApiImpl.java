package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.UserLocation;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/22 16:00
 */
@DubboService
public class UserLocationApiImpl implements UserLocationApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Boolean updateLocation(Long userId, Double longitude, Double latitude, String address) {
        try {
            Query query = Query.query(Criteria.where("userId").is(userId));
            UserLocation location = mongoTemplate.findOne(query, UserLocation.class);
            if(location == null){
                // 保存
                location = new UserLocation();
                location.setLocation(new GeoJsonPoint(longitude, latitude));
                location.setUserId(userId);
                location.setAddress(address);
                location.setCreated(System.currentTimeMillis());
                location.setUpdated(System.currentTimeMillis());
                location.setLastUpdated(System.currentTimeMillis());
                mongoTemplate.save(location);
            } else{
                // 更新
                Update update = Update.update("location", new GeoJsonPoint(longitude, latitude))
                        .set("lastUpdated", location.getUpdated())
                        .set("updated", System.currentTimeMillis());
                mongoTemplate.updateFirst(query, update, UserLocation.class);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }













}
