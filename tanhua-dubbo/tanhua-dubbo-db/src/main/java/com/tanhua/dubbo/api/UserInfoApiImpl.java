package com.tanhua.dubbo.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

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
    public Map<Long, UserInfo> findByIds(List<Long> ids, UserInfo info) {
        QueryWrapper<UserInfo> qw = new QueryWrapper<>();
        qw.in("id", ids);
        // 添加筛选条件
        if(info != null){
            if(info.getAge() != null){
                qw.lt("age", info.getAge());
            }
            if(StrUtil.isNotEmpty(info.getGender())){
                qw.eq("gender", info.getGender());
            }
        }
        List<UserInfo> list = userInfoMapper.selectList(qw);
        Map<Long, UserInfo> infosMap = CollUtil.fieldValueMap(list, "id");
        return infosMap;
    }

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
