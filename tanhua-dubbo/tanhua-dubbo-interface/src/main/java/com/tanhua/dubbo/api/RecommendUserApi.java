package com.tanhua.dubbo.api;

import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.vo.PageResult;

import java.util.List;

public interface RecommendUserApi {

    // 查询今日佳人
    RecommendUser queryWithMaxScore(Long toUserId);

    // 分页查询推荐好友
    PageResult queryRecommendUserByPage(Integer page, Integer pagesize, Long toUserId);

    // 根据userId 、 toUserId查询
    RecommendUser queryByUserIdAndToUserId(Long userId, Long toUserId);

    // 获取推荐列表（排除喜欢、不喜欢用户）
    List<RecommendUser> queryCartsList(Long userId, Integer count);
}
