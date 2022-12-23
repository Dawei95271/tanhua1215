package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Video;

import java.util.List;

public interface VideoApi {

    // 保存小视频
    String save(Video video);

    // 分页查询video
    List<Video> queryVideos(Integer page, Integer pagesize);

    // 查询推荐视频
    List<Video> queryVideosByVids(List<Long> vids);
}
