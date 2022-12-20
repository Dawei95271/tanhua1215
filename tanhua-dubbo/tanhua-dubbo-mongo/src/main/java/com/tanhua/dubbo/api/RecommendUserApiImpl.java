package com.tanhua.dubbo.api;

import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.vo.PageResult;
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
 * @time: 2022/12/19 16:40
 */
@DubboService
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

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
