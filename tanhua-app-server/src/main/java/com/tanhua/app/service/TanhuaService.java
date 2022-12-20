package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.dto.RecommendUserDto;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.TodayBest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    // 分页获取好友推荐列表
    public PageResult recommendation(RecommendUserDto dto) {
        Long toUserId = UserHolder.getUserId();
        PageResult pr = recommendUserApi.queryRecommendUserByPage(dto.getPage(), dto.getPagesize(), toUserId);
        List<RecommendUser> items = (List<RecommendUser>)pr.getItems();
        if(CollUtil.isEmpty(items)){
            return pr;
        }
        // 有数据，则构造返回值
        // 获取ids
        List<Long> ids = CollUtil.getFieldValues(items, "userId", Long.class);
        // 设置查询条件
        UserInfo info = new UserInfo();
        info.setGender(dto.getGender());
        info.setAge(dto.getAge());
        // 查询
        Map<Long, UserInfo> userInfoMap = userInfoApi.findByIds(ids, info);
        // 构造返回值
        List<TodayBest> vos = new ArrayList<>();
        for (RecommendUser item : items) {
            UserInfo userInfo = userInfoMap.get(item.getUserId());
            if(userInfo != null){
                TodayBest vo = TodayBest.init(userInfo, item);
                vos.add(vo);
            }
        }
        pr.setItems(vos);
        return pr;

    }

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
