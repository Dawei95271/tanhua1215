package com.tanhua.app.controller;

import com.tanhua.app.service.CommentsService;
import com.tanhua.model.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 15:51
 */
@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;


    /**
     * 发布评论
     */
    @PostMapping
    public ResponseEntity saveComment(@RequestBody Map map){
        String movementId = (String) map.get("movementId");
        String content = (String) map.get("comment");
        commentsService.saveComment(movementId, content);
        return ResponseEntity.ok(null);

    }

    /**
     * 分页查询评论列表
     */
    @GetMapping
    public ResponseEntity findComments(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pagesize,
                                       String movementId){
        PageResult pr = commentsService.findComments(movementId, page, pagesize);
        return ResponseEntity.ok(pr);
    }
}
