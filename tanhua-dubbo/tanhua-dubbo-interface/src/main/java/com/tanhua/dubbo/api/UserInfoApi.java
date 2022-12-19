package com.tanhua.dubbo.api;

import com.tanhua.model.domain.UserInfo;

public interface UserInfoApi {

    // 保存用户信息
    void save(UserInfo userInfo);

    // 更新用户信息
    void updateById(UserInfo userInfo);

    // 根据id查询userInfo
    UserInfo findById(Long userId);
}
