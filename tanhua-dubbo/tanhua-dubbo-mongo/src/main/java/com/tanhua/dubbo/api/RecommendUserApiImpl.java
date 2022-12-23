package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.mongo.UserLike;
import com.tanhua.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 16:40
 */
@DubboService
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 排除喜欢、不喜欢用户
     * 随机展示
     * 指定数量
     * @param userId
     * @param count
     * @return
     */
    @Override
    public List<RecommendUser> queryCartsList(Long userId, Integer count) {
        // 获取喜欢不喜欢用户id集合
        List<UserLike> userLikeList = mongoTemplate.find(Query.query(Criteria.where("userId").is(userId)), UserLike.class);
        List<Long> likeUserIds = CollUtil.getFieldValues(userLikeList, "likeUserId", Long.class);
        // 获取推荐用户
        Criteria criteria = Criteria.where("toUserId").is(userId)
                .and("userId").nin(likeUserIds);
        // 使用推荐函数，获取推荐数据
        TypedAggregation<RecommendUser> newAggregation = TypedAggregation.newAggregation(RecommendUser.class, Aggregation.match(criteria), Aggregation.sample(count));
        AggregationResults<RecommendUser> result = mongoTemplate.aggregate(newAggregation, RecommendUser.class);
        return result.getMappedResults();
    }

    @Override
    public RecommendUser queryByUserIdAndToUserId(Long userId, Long toUserId) {
        Query query = Query.query(Criteria.where("userId").is(userId)
                .and("toUserId").is(toUserId));
        RecommendUser user = mongoTemplate.findOne(query, RecommendUser.class);
        if(user == null){
            user = new RecommendUser();
            user.setUserId(userId);
            user.setToUserId(toUserId);
            user.setScore(95d);
        }
        return user;
    }

    @Override
    public PageResult queryRecommendUserByPage(Integer page, Integer pagesize, Long toUserId) {
        Query query = Query.query(Criteria.where("toUserId").is(toUserId));
        // 总数量
        long count = mongoTemplate.count(query, RecommendUser.class);
        // 继续设置筛选条件
        query.skip((page - 1) * pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("score")));
        List<RecommendUser> list = mongoTemplate.find(query, RecommendUser.class);
        return new PageResult(page, pagesize, count, list);
    }

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
