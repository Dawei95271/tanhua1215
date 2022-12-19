package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.dubbo.mapper.BlackListMapper;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import com.tanhua.model.domain.BlackList;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:04
 */
@DubboService
public class BlackListApiImpl implements BlackListApi{

    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void removeBlackList(Long userId, Long bUserId) {
        QueryWrapper<BlackList> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("black_user_id", bUserId);
        blackListMapper.delete(qw);
    }

    @Override
    public IPage<UserInfo> blackListByPage(Integer page, Integer pagesize, Long userId) {
        Page pages = new Page(page, pagesize);
        return userInfoMapper.findBlackListByPage(pages, userId);
    }
}
