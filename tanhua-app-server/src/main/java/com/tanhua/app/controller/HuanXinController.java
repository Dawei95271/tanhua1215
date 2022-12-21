package com.tanhua.app.controller;

import com.tanhua.app.service.HuanXinService;
import com.tanhua.model.vo.HuanXinUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:28
 */
@RestController
@RequestMapping("huanxin")
public class HuanXinController {

    @Autowired
    private HuanXinService huanXinService;


    @GetMapping("user")
    public ResponseEntity getUser(){
        HuanXinUserVo vo = huanXinService.getUser();
        return ResponseEntity.ok(vo);
    }
}
