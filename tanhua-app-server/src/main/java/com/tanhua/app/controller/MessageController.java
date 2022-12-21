package com.tanhua.app.controller;

import com.tanhua.app.service.MessagesService;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:33
 */
@RestController
@RequestMapping("messages")
public class MessageController {

    @Autowired
    private MessagesService messagesService;

    /**
     * 联系人列表
     */
    @GetMapping("contacts")
    public ResponseEntity contacts(@RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer pagesize,
                                   @RequestParam(required = false) String keyword){
        PageResult pr = messagesService.contacts(page, pagesize, keyword);
        return ResponseEntity.ok(pr);
    }

    /**
     * 联系人添加
     */
    @PostMapping("contacts")
    public ResponseEntity contacts(@RequestBody Map map){
        Long friendId = Long.parseLong(map.get("userId").toString());
        messagesService.contacts(friendId);
        return ResponseEntity.ok(null);
    }

    /**
     * 根据环信id查询用户信息
     */
    @GetMapping("userinfo")
    public ResponseEntity userinfo(String huanxinId){
        UserInfoVo vo = messagesService.findByHxId(huanxinId);
        return ResponseEntity.ok(vo);
    }
}
