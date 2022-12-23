package com.tanhua.admin.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.tanhua.admin.service.AdminService;
import com.tanhua.commons.utils.Constants;
import com.tanhua.model.admin.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/system/users")
public class SystemController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 登出
     */
    @PostMapping("logout")
    public ResponseEntity logout(){
        return ResponseEntity.ok(null);
    }

    /**
     * 用户基本信息
     */
    @PostMapping("profile")
    public ResponseEntity profile(){
        Admin admin = adminService.profile();
        return ResponseEntity.ok(admin);
    }

    /**
     * 用户登录
     */
    @PostMapping("login")
    public ResponseEntity login(@RequestBody Map map){
        Map retMap = adminService.login(map);
        return ResponseEntity.ok(retMap);
    }

    /**
     * 获取验证码图片
     */
    @GetMapping("verification")
    public void verification(String uuid, HttpServletResponse response) throws IOException {
        // hutool工具中有验证码工具
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(299, 99);
        String code = lineCaptcha.getCode();
        // 存入redis
        redisTemplate.opsForValue().set(Constants.CAP_CODE + uuid, code);
        // 写入response
        lineCaptcha.write(response.getOutputStream());
    }

}
