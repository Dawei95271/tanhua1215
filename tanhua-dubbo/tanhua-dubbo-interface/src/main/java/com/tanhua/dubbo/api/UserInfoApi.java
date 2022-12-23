package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.model.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoApi {

    // 保存用户信息
    void save(UserInfo userInfo);

    // 更新用户信息
    void updateById(UserInfo userInfo);

    // 根据id查询userInfo
    UserInfo findById(Long userId);

    // 根据ids，获取map集合
    Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo info);

    // 分页查询用户
    IPage<UserInfo> findAllByPage(Integer page, Integer pagesize);
}
