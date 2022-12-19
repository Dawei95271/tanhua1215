package com.tanhua.app.controller;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.app.service.LoginService;
import com.tanhua.app.service.SettingService;
import com.tanhua.model.domain.Settings;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.SettingsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:07
 */
@RestController
@RequestMapping("users")
public class SettingController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private LoginService loginService;

    /**
     * 修改手机号-3
     * @return
     */
    @PostMapping("phone")
    public ResponseEntity savePhone(@RequestBody Map map){
        String phone = (String) map.get("phone");
        settingService.savePhone(phone);
        return ResponseEntity.ok(null);
    }

    /**
     * 修改手机号-2
     * @return
     */
    @PostMapping("phone/checkVerificationCode")
    public ResponseEntity checkVerificationCode(@RequestBody Map map){
        String code = (String) map.get("verificationCode");
        Map retMap = settingService.verificationCode(UserHolder.getMobile(), code);
        return ResponseEntity.ok(retMap);
    }

    /**
     * 修改手机号-1
     * @return
     */
    @PostMapping("phone/sendVerificationCode")
    public ResponseEntity sendVerificationCode(){
        loginService.sendMsg(UserHolder.getMobile());
        return ResponseEntity.ok(null);
    }

    /**
     * 移除黑名单
     * @param bUserId
     * @return
     */
    @DeleteMapping("blacklist/{uid}")
    public ResponseEntity blackList(@PathVariable("uid") Long bUserId){
        settingService.removeBlackList(bUserId);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询黑名单
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping("blacklist")
    public ResponseEntity blackList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pagesize", required = false, defaultValue = "10") Integer pagesize){
        PageResult pr = settingService.blackList(page, pagesize);
        return ResponseEntity.ok(pr);
    }


    /**
     * 通用设置更新
     * @param settings
     * @return
     */
    @PostMapping("notifications/setting")
    public ResponseEntity saveSettings(@RequestBody Settings settings){
        settingService.saveSettings(settings);
        return ResponseEntity.ok(null);
    }

    /**
     * 设置陌生人问题
     * @param map
     * @return
     */
    @PostMapping("questions")
    public ResponseEntity questions(@RequestBody Map map){
        String content = (String) map.get("content");
        settingService.saveQuestions(content);
        return ResponseEntity.ok(null);
    }

    /**
     * 通用设置查询
     * @return
     */
    @GetMapping("settings")
    public ResponseEntity settings(){
        SettingsVo vo = settingService.settings();
        return ResponseEntity.ok(vo);
    }
}
