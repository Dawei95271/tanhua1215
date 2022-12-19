package com.tanhua.app.service;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.UserInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 11:14
 */
@Service
public class UsersService {

    @DubboReference
    private UserInfoApi userInfoApi;

    public void updateUserInfo(UserInfo userInfo) {
        Long userId = UserHolder.getUserId();
        userInfo.setId(userId);
        userInfoApi.updateById(userInfo);
    }

    public UserInfoVo users(Long userId) {
        if(userId == null){
            userId = UserHolder.getUserId();
        }
        UserInfo info = userInfoApi.findById(userId);
        UserInfoVo vo = new UserInfoVo();
        // 复制
        BeanUtils.copyProperties(info, vo);
        if(info.getAge() != null){
            vo.setAge(info.getAge().toString());

        }

        return vo;
    }


    public void header(MultipartFile headPhoto) {


    }





}
