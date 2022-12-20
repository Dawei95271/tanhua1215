package com.tanhua.app.controller;

import com.tanhua.app.service.TanhuaService;
import com.tanhua.model.dto.RecommendUserDto;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.TodayBest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
