package com.tanhua.admin.controller;

import com.tanhua.admin.service.ManageService;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.vo.MovementsVo;
import com.tanhua.model.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/23 18:19
 */
@RestController
@RequestMapping("manage")
public class ManagerController {

    @Autowired
    private ManageService manageService;

    /**
     * 获取评论列表
     */
    @GetMapping("messages/comments")
    public ResponseEntity getComments(@RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pagesize,
                                      String messageID){
        PageResult pr = manageService.getComments(messageID, page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 动态详情
     */
    @GetMapping("messages/{id}")
    public ResponseEntity getMovement(@PathVariable String id){
        MovementsVo vo = manageService.getMovement(id);
        return ResponseEntity.ok(vo);
    }

    /**
     * 动态分页查询
     */
    @GetMapping("messages")
    public ResponseEntity getMovementByPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pagesize,
                                       Long uid, Integer state){
        PageResult pr = manageService.getMovements(uid, state, page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 视频翻页-分页查询
     */
    @GetMapping("videos")
    public ResponseEntity getVideos(@RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer pagesize,
                                    Long uid){
        PageResult pr = manageService.getVideos(uid, page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 分页查询用户数据
     */
    @GetMapping("users")
    public ResponseEntity findAll(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer pagesize){
        PageResult pr = manageService.findAll(page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 用户基本资料
     */
    @GetMapping("users/{userId}")
    public ResponseEntity getUser(@PathVariable Long userId){
        UserInfo info = manageService.getUser(userId);
        return ResponseEntity.ok(info);
    }
}
