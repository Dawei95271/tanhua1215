package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mapper.UserMapper;
import com.tanhua.model.domain.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/16 17:12
 */
@DubboService
public class UserApiImpl implements UserApi{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByHxId(String huanxinId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("hx_user", huanxinId);

        return userMapper.selectOne(qw);
    }

    @Override
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public Long save(User user) {
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public User findByMobile(String mobile) {
        // 已有用户的登录
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("mobile", mobile);
        return userMapper.selectOne(qw);
    }
}
