package com.tanhua.dubbo.api;

import com.tanhua.model.mongo.Movement;
import com.tanhua.model.vo.PageResult;

import java.util.List;

public interface MovementApi {

    // 发布动态
    void publish(Movement movement);

    // 分页查询用户动态
    PageResult findByUserId(Long userId, Integer page, Integer pagesize);

    // 分页查询好友动态
    PageResult findFriendMovement(Long userId, Integer page, Integer pagesize);

    // 根据pids查询movement
    List<Movement> findMovementByPids(List<Long> pids);

    // 随机获取动态
    List<Movement> randomMovement(Integer pagesize);

    // 查询单个动态
    Movement findById(String movementId);

    // 根据userId、state查询动态
    PageResult findMovementByUidAndState(Long uid, Integer state, Integer page, Integer pagesize);
}
