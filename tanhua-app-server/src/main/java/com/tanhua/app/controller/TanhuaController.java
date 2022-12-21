package com.tanhua.app.controller;

import com.tanhua.app.service.TanhuaService;
import com.tanhua.model.dto.RecommendUserDto;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 17:13
 */
@RestController
@RequestMapping("tanhua")
public class TanhuaController {

    @Autowired
    private TanhuaService tanhuaService;

    /**
     * 回复陌生人问题
     */
    @PostMapping("strangerQuestions")
    public ResponseEntity strangerQuestions(@RequestBody Map map){
        Long userId = Long.valueOf(map.get("userId").toString());
        String reply = (String) map.get("reply");
        tanhuaService.strangerQuestions(userId, reply);
        return ResponseEntity.ok(null);
    }

    /**
     * 查询陌生人问题
     */
    @GetMapping("strangerQuestions")
    public ResponseEntity strangerQuestions(Long userId){
        String content = tanhuaService.strangerQuestions(userId);
        return ResponseEntity.ok(content);
    }

    /**
     * 查看佳人信息
     */
    @GetMapping("{id}/personalInfo")
    public ResponseEntity personalInfo(@PathVariable("id") Long userId){
        TodayBest vo = tanhuaService.personalInfo(userId);
        return ResponseEntity.ok(vo);
    }

    /**
     * 分页好友推荐列表
     */
    @GetMapping("recommendation")
    public ResponseEntity recommendation(RecommendUserDto dto){
        PageResult pr = tanhuaService.recommendation(dto);
        return ResponseEntity.ok(pr);
    }

    /**
     * 今日佳人
     */
    @GetMapping("todayBest")
    public ResponseEntity todayBest(){
        TodayBest vo = tanhuaService.todayBest();
        return ResponseEntity.ok(vo);
    }



}
