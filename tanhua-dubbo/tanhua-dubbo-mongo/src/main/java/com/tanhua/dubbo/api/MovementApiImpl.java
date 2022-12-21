package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.service.TimeLineService;
import com.tanhua.dubbo.utils.IdWorker;
import com.tanhua.model.mongo.Friend;
import com.tanhua.model.mongo.Movement;
import com.tanhua.model.mongo.MovementTimeLine;
import com.tanhua.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/20 17:35
 */
@DubboService
public class MovementApiImpl implements MovementApi{

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TimeLineService timeLineService;

    @Override
    public Movement findById(String movementId) {
        return mongoTemplate.findById(movementId, Movement.class);
    }

    @Override
    public List<Movement> randomMovement(Integer pagesize) {
        // 统计对象
        TypedAggregation<Movement> aggregation = Aggregation.newAggregation(Movement.class, Aggregation.sample(pagesize));
        // 统计结果
        AggregationResults<Movement> result = mongoTemplate.aggregate(aggregation, Movement.class);
        return result.getMappedResults();
    }

    @Override
    public List<Movement> findMovementByPids(List<Long> pids) {
        Query query = Query.query(Criteria.where("pid").in(pids));
        return mongoTemplate.find(query, Movement.class);
    }

    @Override
    public PageResult findFriendMovement(Long userId, Integer page, Integer pagesize) {
        // 根据时间线查询的，
        // 时间线里有自己，动态可见
        Query query = Query.query(Criteria.where("friendId").is(userId));
        long count = mongoTemplate.count(query, MovementTimeLine.class);
        query.skip((page - 1) * pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));
        // 查询时间线
        List<MovementTimeLine> lines = mongoTemplate.find(query, MovementTimeLine.class);
        List<ObjectId> ids = CollUtil.getFieldValues(lines, "movementId", ObjectId.class);
        // 查询动态
        Query movementQuery = Query.query(Criteria.where("id").in(ids));
        List<Movement> list = mongoTemplate.find(movementQuery, Movement.class);
        return new PageResult(page, pagesize, count, list);

    }

    @Override
    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {

        Query query = Query.query(Criteria.where("userId").is(userId));
        // 先查数量
        long count = mongoTemplate.count(query, Movement.class);

        query.skip((page - 1) * pagesize)
                .limit(pagesize)
                .with(Sort.by(Sort.Order.desc("create")));
        List<Movement> list = mongoTemplate.find(query, Movement.class);
        return new PageResult(page, pagesize, count, list);
    }

    @Override
    public void publish(Movement movement) {
        try {
            movement.setPid(idWorker.getNextId("movement1215"));
            movement.setCreated(new Date().getTime());
            // 保存动态
            mongoTemplate.save(movement);
            // 保存时间线
            timeLineService.saveTimeLine(movement.getUserId(), movement.getId());
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }

    }



}
