package com.tanhua.dubbo.api;

import com.tanhua.dubbo.utils.IdWorker;
import com.tanhua.model.mongo.Video;
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
 * @time: 2022/12/23 11:29
 */
@DubboService
public class VideoApiImpl implements VideoApi{

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdWorker idWorker;


    @Override
    public List<Video> queryVideos(Integer page, Integer pagesize) {
        Query query = new Query();
        query.skip((page - 1) * pagesize).limit(pagesize)
                .with(Sort.by(Sort.Order.desc("created")));

        return mongoTemplate.find(query, Video.class);
    }

    @Override
    public List<Video> queryVideosByVids(List<Long> vids) {
        Query query = Query.query(Criteria.where("vid").in(vids));

        return mongoTemplate.find(query, Video.class);
    }

    @Override
    public String save(Video video) {
        video.setVid(idWorker.getNextId("video"));
        video.setCreated(System.currentTimeMillis());
        mongoTemplate.save(video);

        return video.getId().toHexString();
    }





}
