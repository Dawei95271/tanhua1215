package com.tanhua.app.service;


import com.tanhua.app.exception.BusinessException;
import com.tanhua.autoconfig.template.SmsTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/16 16:29
 */

@Service
public class LoginService {

    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @DubboReference
    private UserApi userApi;

    // 验证登录
    public Map loginVerification(String phone, String code) {
        String value = redisTemplate.opsForValue().get(Constants.SMS_CODE + phone);
        // 验证码失效
        if(value == null){
            throw new BusinessException(ErrorResult.loginError());
        }
        // 验证码错误
        if(! value.equals(code)){
            throw new BusinessException(ErrorResult.verifyError());
        }

        // 验证成功
        redisTemplate.delete(Constants.SMS_CODE + phone);

        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        if(user == null){
            // new user
            user = new User();
            user.setMobile(phone);
            user.setPassword(Constants.INIT_PASSWORD);
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;
        }
        // token
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", user.getMobile());
        map.put("userId", user.getId());
        String token = JwtUtils.getToken(map);
        // 返回值
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("token", token);
        retMap.put("isNew", isNew);
        return retMap;

    }


    // 发送短信验证码
    public void sendMsg(String phone) {
        // 生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        // 发送
        smsTemplate.sendSms(phone, code);
        // 存入redis,5分钟失效
        String redisKey = Constants.SMS_CODE + phone;
        redisTemplate.opsForValue().set(redisKey, code, Constants.SMS_TIME, TimeUnit.MINUTES);


    }



}
