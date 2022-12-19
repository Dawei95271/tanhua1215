package com.tanhua.dubbo.api;

import com.tanhua.model.domain.User;

public interface UserApi {

    //根据手机号码查询用户
    User findByMobile(String mobile);

    // 保存user，返回userId
    Long save(User user);

    // 根据id更新
    void update(User user);
}
