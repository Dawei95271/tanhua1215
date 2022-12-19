package com.tanhua.app.controller;

import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.app.service.UserInfoService;
import com.tanhua.app.service.UsersService;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 11:11
 */
@RestController
@RequestMapping("users")
public class UsersController {


    @Autowired
    private UsersService usersService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 上传用户头像
     */
    @PostMapping("header")
    public ResponseEntity header(@RequestParam MultipartFile headPhoto){
        userInfoService.head(headPhoto);
        return ResponseEntity.ok(null);
    }

    /**
     * 跟新用户资料
     */
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody UserInfo userInfo){
        usersService.updateUserInfo(userInfo);
        return ResponseEntity.ok(null);
    }

    /**
     * 查询用户资料
     * @param userId
     * @return
     */
    @GetMapping
    public ResponseEntity users(@RequestParam(value = "userID", required = false) Long userId){
        UserInfoVo vo = usersService.users(userId);
        return ResponseEntity.ok(vo);
    }
}
