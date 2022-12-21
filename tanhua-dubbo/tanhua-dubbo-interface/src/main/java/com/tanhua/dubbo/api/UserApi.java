package com.tanhua.dubbo.api;

import com.tanhua.model.domain.User;

import java.util.List;

public interface UserApi {

    //根据手机号码查询用户
    User findByMobile(String mobile);

    // 保存user，返回userId
    Long save(User user);

    // 根据id更新
    void update(User user);

    // 获取所有user
    List<User> findAll();

    // 根据id查询
    User findById(Long userId);

    // 根据环信id查询user
    User findByHxId(String huanxinId);
}
