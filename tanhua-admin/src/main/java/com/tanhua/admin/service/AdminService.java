package com.tanhua.admin.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.admin.exception.BusinessException;
import com.tanhua.admin.interceptor.AdminHolder;
import com.tanhua.admin.mapper.AdminMapper;
import com.tanhua.commons.utils.Constants;
import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.model.admin.Admin;
import org.apache.catalina.security.SecurityUtil;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired(required = false)
    private AdminMapper adminMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 登录
    public Map login(Map map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String verificationCode = (String) map.get("verificationCode");
        String uuid = (String) map.get("uuid");
        // 用户名密码控制判断
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            throw new BusinessException("用户名或密码不能为空");
        }
        // 判断验证码空值
        if(StrUtil.isEmpty(uuid)){
            throw new BusinessException("验证码不能为空");
        }
        // 判断redis中uuid值 是否相等
        String value = redisTemplate.opsForValue().get(Constants.CAP_CODE + uuid);
        if(StrUtil.isEmpty(value) || ! value.equals(verificationCode)){
            throw new BusinessException("验证码错误");
        }
        redisTemplate.delete(Constants.CAP_CODE + uuid);
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("username", username);
        Admin admin = adminMapper.selectOne(qw);
        if(admin == null){
            throw new BusinessException("用户不存在");
        }
        if(! admin.getPassword().equals(SecureUtil.md5(password))){
            throw new BusinessException("密码错误");
        }
        // 构建token
        Map<String, Object> paramsMap = new HashMap();
        paramsMap.put("id", admin.getId());
        paramsMap.put("username", username);
        String token = JwtUtils.getToken(paramsMap);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("token", token);
        return retMap;
    }


    // 获取基本信息
    public Admin profile() {
        Long userId = AdminHolder.getUserId();
        return adminMapper.selectById(userId);
    }














}
