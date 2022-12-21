package com.tanhua.dubbo.api;

import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.vo.PageResult;

public interface RecommendUserApi {

    // 查询今日佳人
    RecommendUser queryWithMaxScore(Long toUserId);

    // 分页查询推荐好友
    PageResult queryRecommendUserByPage(Integer page, Integer pagesize, Long toUserId);

    // 根据userId 、 toUserId查询
    RecommendUser queryByUserIdAndToUserId(Long userId, Long toUserId);
}
