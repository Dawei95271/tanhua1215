package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Friend;

import java.util.List;

public interface FriendApi {

    // 保存好友关系
    void save(Long userId, Long friendId);

    // 分页查询friendList
    List<Friend> findByUserId(Long userId, Integer page, Integer pagesize);
}
