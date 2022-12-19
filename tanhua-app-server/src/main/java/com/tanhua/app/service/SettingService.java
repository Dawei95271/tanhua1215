package com.tanhua.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.*;
import com.tanhua.model.domain.Question;
import com.tanhua.model.domain.Settings;
import com.tanhua.model.domain.User;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.SettingsVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:11
 */
@Service
public class SettingService {

    @DubboReference
    private QuestionsApi questionsApi;
    @DubboReference
    private UserInfoApi userInfoApi;
    @DubboReference
    private SettingApi settingApi;
    @DubboReference
    private BlackListApi blackListApi;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @DubboReference
    private UserApi userApi;

    // 保存手机号
    public void savePhone(String phone) {
        User user = userApi.findByMobile(phone);
        if(user == null){
            // 新的手机号不能已经被绑定
            user = new User();
            user.setMobile(phone);
            user.setId(UserHolder.getUserId());
            userApi.update(user);
        } else{
            throw new BusinessException(ErrorResult.mobileError());
        }
    }

    // 跟换手机号，验证码校验
    public Map verificationCode(String mobile, String code) {
        String key = Constants.SMS_CODE + mobile;
        String value = redisTemplate.opsForValue().get(key);
        Map retMap = new HashMap();
        if(value == null || ! value.equals(code)){
            retMap.put("verification", false);
        }else{
            // 验证码正确，删除
            redisTemplate.delete(key);
            retMap.put("verification", true);
        }
        return retMap;
    }

    // 一查黑名单
    public void removeBlackList(Long bUserId) {
        Long userId = UserHolder.getUserId();
        blackListApi.removeBlackList(userId, bUserId);
    }

    // 分页查询黑名单
    public PageResult blackList(Integer page, Integer pagesize) {
        Long userId = UserHolder.getUserId();
        IPage<UserInfo> ipage = blackListApi.blackListByPage(page, pagesize, userId);
        PageResult pr = new PageResult(page, pagesize, ipage.getTotal(), ipage.getRecords());
        return pr;
    }

    // 获取通用设置
    public SettingsVo settings() {
        Long userId = UserHolder.getUserId();
        String mobile = UserHolder.getMobile();
        SettingsVo vo = new SettingsVo();
        vo.setId(userId);
        vo.setPhone(mobile);
        // 设置settings
        Settings settings = settingApi.findByUserId(userId);
        if(settings != null){
            BeanUtils.copyProperties(settings, vo);
        }
        // 设置陌生人问题
        Question question = questionsApi.findByUserId(userId);
        String txt = question.getTxt() == null ? "你喜欢java吗": question.getTxt();
        vo.setStrangerQuestion(txt);

        return vo;
    }

    public void saveQuestions(String content) {
        Long userId = UserHolder.getUserId();
        Question question = questionsApi.findByUserId(userId);
        if(question == null){
            question = new Question();
            question.setTxt(content);
            question.setUserId(userId);
            questionsApi.saveQuestions(question);
        } else{
            question.setTxt(content);
            questionsApi.updateById(question);
        }

    }


    public void saveSettings(Settings settings) {

        Long userId = UserHolder.getUserId();
        Settings search = settingApi.findByUserId(userId);
        if(search == null){
            settings.setUserId(userId);
            settingApi.save(settings);
        } else{
            settings.setId(search.getId());
            settingApi.update(settings);
        }

    }



}
