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
import com.tanhua.dubbo.api.UserLikeApi;
import com.tanhua.model.domain.Question;
import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.dto.RecommendUserDto;
import com.tanhua.model.mongo.UserLocation;
import com.tanhua.model.vo.NearUserVo;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.TodayBest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    @DubboReference
    private UserLikeApi  userLikeApi;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MessagesService messagesService;

    @Value("${tanhua.default.recommend.users}")
    private String recommendUser;

    // 搜附近
    public List<NearUserVo> search(String gender, String distance) {
        // 查询附近用户
        List<Long> ids = userLikeApi.queryNear(UserHolder.getUserId(), Double.valueOf(distance));
        if(CollUtil.isEmpty(ids)){
            return null;
        }
        // 查询用户信息
        UserInfo info = new UserInfo();
        info.setGender(gender);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, info);
        // 封装vo返回
        List<NearUserVo> vos = new ArrayList<>();
        for (Long id : ids) {
            UserInfo userInfo = infoMap.get(id);
            if(id == UserHolder.getUserId()){
                // 排除自己
                continue;
            }
            if(userInfo != null){
                NearUserVo vo = NearUserVo.init(userInfo);
                vos.add(vo);
            }
        }
        return vos;
    }

    // 喜欢
    public void likeUser(Long likeUserId) {
        Boolean save = userLikeApi.saveOrUpdate(UserHolder.getUserId(), likeUserId, true);
        if(! save){
            throw new BusinessException(ErrorResult.error());
        }
        // 修改redis中数据
        redisTemplate.opsForSet().remove(Constants.USER_NOT_LIKE_KEY + UserHolder.getUserId(), likeUserId.toString());
        redisTemplate.opsForSet().add(Constants.USER_LIKE_KEY + UserHolder.getUserId(), likeUserId.toString());
        // 双向喜欢，则添加好友关系
        if(isLike(likeUserId, UserHolder.getUserId())){
            messagesService.contacts(likeUserId);
        }
    }



    // 不喜欢
    public void notLikeUser(Long likeUserId) {
        Boolean save = userLikeApi.saveOrUpdate(UserHolder.getUserId(), likeUserId, false);
        if(! save){
            throw new BusinessException(ErrorResult.error());
        }
        redisTemplate.opsForSet().remove(Constants.USER_LIKE_KEY + UserHolder.getUserId(), likeUserId.toString());
        redisTemplate.opsForSet().add(Constants.USER_NOT_LIKE_KEY + UserHolder.getUserId(), likeUserId.toString());

    }

    // 判断对方是否喜欢我
    private boolean isLike(Long likeUserId, Long userId) {
        String key = Constants.USER_LIKE_KEY + likeUserId;
        return redisTemplate.opsForSet().isMember(key, userId.toString());
    }



    // 探花-左滑右滑
    public List<TodayBest> carts() {
        // 获取推荐用户列表
        List<RecommendUser> users = recommendUserApi.queryCartsList(UserHolder.getUserId(), 10);
        if(CollUtil.isEmpty(users)){
            users = new ArrayList<>();
            String[] userIds = recommendUser.split(",");
            for (String userId : userIds) {
                RecommendUser user = new RecommendUser();
                user.setUserId(Long.parseLong(userId));
                user.setToUserId(UserHolder.getUserId());
                user.setScore(RandomUtils.nextDouble(70, 95));
                users.add(user);
            }
        }
        // 构造vo返回
        List<Long> ids = CollUtil.getFieldValues(users, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, null);

        List<TodayBest> list = new ArrayList<>();
        for (RecommendUser user : users) {
            UserInfo userInfo = infoMap.get(user.getUserId());
            if(userInfo != null){
                TodayBest vo = TodayBest.init(userInfo, user);
                list.add(vo);
            }
        }
        return list;
    }

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
