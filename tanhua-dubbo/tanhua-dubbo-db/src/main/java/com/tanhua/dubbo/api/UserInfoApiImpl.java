package com.tanhua.dubbo.api;

import com.tanhua.dubbo.mapper.UserInfoMapper;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 17:55
 */

@DubboService
public class UserInfoApiImpl implements UserInfoApi{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findById(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public void updateById(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }


}
