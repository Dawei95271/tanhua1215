package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.autoconfig.template.HuanXinTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.QuestionsApi;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.Question;
import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.dto.RecommendUserDto;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.TodayBest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    @DubboReference
    private QuestionsApi questionsApi;
    @Autowired
    private HuanXinTemplate huanXinTemplate;

    // 回复陌生人问题
    public void strangerQuestions(Long userId, String reply) {
        Long currentUserId = UserHolder.getUserId();
        UserInfo info = userInfoApi.findById(currentUserId);
        // 封装数据
        Map<String, Object> map = new HashMap();
        map.put("userId", currentUserId);
        map.put("huanXinId", Constants.HX_USER_PREFIX + currentUserId);
        map.put("nickname", info.getNickname());
        map.put("strangerQuestion", strangerQuestions(userId));
        map.put("reply", reply);

        String message = JSON.toJSONString(map);
        // 回复问题
        Boolean ret = huanXinTemplate.sendMsg(Constants.HX_USER_PREFIX + userId, message);
        if(! ret){
            throw new BusinessException(ErrorResult.error());
        }
    }

    // 查询陌生人问题
    public String strangerQuestions(Long userId) {
        Question question = questionsApi.findByUserId(userId);
        return question == null ? "你喜欢java吗" : question.getTxt();
    }

    // 查看佳人信息
    public TodayBest personalInfo(Long userId) {
        UserInfo info = userInfoApi.findById(userId);
        RecommendUser user = recommendUserApi.queryByUserIdAndToUserId(userId, UserHolder.getUserId());
        return TodayBest.init(info, user);
    }


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
