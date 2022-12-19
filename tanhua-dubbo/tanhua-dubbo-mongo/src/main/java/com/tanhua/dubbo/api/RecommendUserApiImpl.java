package com.tanhua.dubbo.api;

import com.tanhua.model.domain.RecommendUser;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 16:40
 */
@DubboService
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryWithMaxScore(Long toUserId) {
        // 查询条件
        Criteria criteria = Criteria.where("toUserId").is(toUserId);
        // 查询完成之后的条件过滤
        Query query = Query.query(criteria).with(Sort.by(Sort.Order.desc("score")))
                .limit(1);
        // 调用api
        RecommendUser one = mongoTemplate.findOne(query, RecommendUser.class);
        // 返回
        return one;
    }
}
