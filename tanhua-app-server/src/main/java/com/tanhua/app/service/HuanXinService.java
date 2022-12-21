package com.tanhua.app.service;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import com.tanhua.model.vo.HuanXinUserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:30
 */
@Service
public class HuanXinService {

    @DubboReference
    private UserApi userApi;

    // 获取用户环信账号、密码
    public HuanXinUserVo getUser() {
        User user = userApi.findById(UserHolder.getUserId());
        if(user == null){
            return null;
        }
        return new HuanXinUserVo(user.getHxUser(), user.getHxPassword());
    }
}
