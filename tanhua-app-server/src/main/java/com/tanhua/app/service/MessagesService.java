package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.autoconfig.template.HuanXinTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.User;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.mongo.Friend;
import com.tanhua.model.vo.ContactVo;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.UserInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:37
 */
@Service
public class MessagesService {

    @DubboReference
    private UserApi userApi;
    @DubboReference
    private UserInfoApi userInfoApi;
    @Autowired
    private HuanXinTemplate huanXinTemplate;
    @DubboReference
    private FriendApi friendApi;


    // 分页查询联系人列表
    public PageResult contacts(Integer page, Integer pagesize, String keyword) {
        List<Friend> friendList = friendApi.findByUserId(UserHolder.getUserId(), page, pagesize);
        if(CollUtil.isEmpty(friendList)){
            return new PageResult();
        }

        List<Long> ids = CollUtil.getFieldValues(friendList, "friendId", Long.class);
        // 查询条件
        UserInfo info = new UserInfo();
        info.setNickname(keyword);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, info);
        List<ContactVo> vos = new ArrayList<>();
        for (Friend friend : friendList) {
            UserInfo userInfo = infoMap.get(friend.getFriendId());
            if(userInfo != null){
                ContactVo vo = ContactVo.init(userInfo);
                vos.add(vo);
            }
        }
        return new PageResult(page, pagesize, 0l, vos);
    }

    // 添加联系人
    public void contacts(Long friendId) {
        Long userId = UserHolder.getUserId();
        // 环信添加好友关系
        Boolean result = huanXinTemplate.addContact(Constants.HX_USER_PREFIX + friendId,
                Constants.HX_USER_PREFIX + userId);
        if(! result){
            // 未添加成功
            throw new BusinessException(ErrorResult.error());
        }
        // 数据库保存
        friendApi.save(userId, friendId);
    }

    // 根据环信id获取用户信息
    public UserInfoVo findByHxId(String huanxinId) {
        User user = userApi.findByHxId(huanxinId);
        UserInfo info = userInfoApi.findById(user.getId());
        UserInfoVo vo = UserInfoVo.init(info);
        return vo;
    }



}
