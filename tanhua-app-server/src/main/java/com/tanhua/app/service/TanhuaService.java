package com.tanhua.app.service;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.TodayBest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 17:52
 */
@Service
public class TanhuaService {

    @DubboReference
    private UserInfoApi userInfoApi;
    @DubboReference
    private RecommendUserApi recommendUserApi;

    // 查询今日佳人
    public TodayBest todayBest() {
        Long toUserId = UserHolder.getUserId();
        RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(toUserId);
        if(recommendUser == null){
            // 未推荐，随机给
            recommendUser = new RecommendUser();
            recommendUser.setUserId(1l);
            recommendUser.setScore(90d);
        }
        // 查询info
        UserInfo info = userInfoApi.findById(recommendUser.getUserId());
        return TodayBest.init(info, recommendUser);
    }
}
