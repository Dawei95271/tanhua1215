package com.tanhua.dubbo.api;

import com.tanhua.model.domain.RecommendUser;

public interface RecommendUserApi {

    // 查询今日佳人
    RecommendUser queryWithMaxScore(Long toUserId);
}
