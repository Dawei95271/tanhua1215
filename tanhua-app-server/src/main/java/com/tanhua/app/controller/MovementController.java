package com.tanhua.app.controller;

import com.tanhua.app.service.MovementService;
import com.tanhua.model.mongo.Movement;
import com.tanhua.model.vo.MovementsVo;
import com.tanhua.model.vo.PageResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/20 17:21
 */

@RestController
@RequestMapping("movements")
public class MovementController {

    @Autowired
    private MovementService movementService;

    /**
     * 单条动态
     */
    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable("id") String movementId){
        MovementsVo vo = movementService.findById(movementId);
        return ResponseEntity.ok(vo);
    }

    /**
     * 推荐动态
     */
    @GetMapping("recommend")
    public ResponseEntity recommend(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pagesize){
        PageResult pr = movementService.recommend(page, pagesize);
        return ResponseEntity.ok(pr);
    }
    /**
     * 好友动态
     */
    @GetMapping
    public ResponseEntity friendMovement(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pagesize){
        PageResult pr = movementService.findFriendMovement(page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 我的动态
     */
    @GetMapping("all")
    public ResponseEntity findByUserId(Long userId,
                              @RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer pagesize){
        PageResult pr = movementService.findByUserId(userId, page, pagesize);
        return ResponseEntity.ok(pr);
    }

    /**
     * 发布动态
     */
    @PostMapping
    public ResponseEntity movements(Movement movement,
                                    MultipartFile[] imageContent) throws IOException {
        movementService.publishMovement(movement, imageContent);
        return ResponseEntity.ok(null);
    }
}
