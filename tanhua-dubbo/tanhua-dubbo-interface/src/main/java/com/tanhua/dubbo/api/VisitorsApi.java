package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Visitors;

import java.util.List;

public interface VisitorsApi {

    // 查询date之后的访问列表
    List<Visitors> queryVisitors(Long userId, Long date);
}
