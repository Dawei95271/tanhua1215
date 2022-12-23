package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.UserLocation;

import java.util.List;

public interface UserLikeApi {
    // 保存或更新
    Boolean saveOrUpdate(Long userId, Long likeUserId, boolean isLike);

    // 根据distance查询userid附近的人
    List<Long> queryNear(Long userId, Double distance);
}
