package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Visitors;
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
 * @time: 2022/12/23 10:40
 */
@DubboService
public class VisitorsApiImpl implements VisitorsApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Visitors> queryVisitors(Long userId, Long date) {
        Criteria criteria = Criteria.where("userId").is(userId);
        if(date != null){
            criteria.and("date").gt(date);
        }
        // 指定日期之后的5条数据
        Query query = Query.query(criteria).limit(5)
                .with(Sort.by(Sort.Order.desc("date")));
        return mongoTemplate.find(query, Visitors.class);
    }




}
