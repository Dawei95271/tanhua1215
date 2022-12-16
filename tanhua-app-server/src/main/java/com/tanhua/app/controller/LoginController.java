package com.tanhua.app.controller;

import com.tanhua.app.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
