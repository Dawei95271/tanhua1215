package com.tanhua.app.controller;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.app.service.LoginService;
import com.tanhua.app.service.UserInfoService;
import com.tanhua.model.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/16 16:20
 */

@RestController
@RequestMapping("/user")
public class LoginController {


    @Autowired
    private LoginService loginService;
    @Autowired
    private UserInfoService userInfoService;

    /*
       04 首次登录---补充头像
    */
    @PostMapping("loginReginfo/head")
    public ResponseEntity head(@RequestParam MultipartFile headPhoto){
        userInfoService.head(headPhoto);
        return ResponseEntity.ok(null);
    }



    /*
        03 首次登录---完善资料
     */
    @PostMapping("loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfo userInfo){
        Long userId = UserHolder.getUserId();
        userInfo.setId(userId);
        userInfoService.loginReginfo(userInfo);
        return ResponseEntity.ok(null);
    }

    /*
        02 登录---验证码校验
     */
    @PostMapping("loginVerification")
    public ResponseEntity loginVerification(@RequestBody Map map){
        String phone = (String) map.get("phone");
        String code = (String) map.get("verificationCode");

        Map retMap = loginService.loginVerification(phone, code);
        return ResponseEntity.ok(retMap);

    }

    /*
        01 登录---获取验证码
     */
    @PostMapping("login")
    public ResponseEntity login(@RequestBody Map map){

        String phone = (String) map.get("phone");
        loginService.sendMsg(phone);
        return ResponseEntity.ok(null);
    }
}
