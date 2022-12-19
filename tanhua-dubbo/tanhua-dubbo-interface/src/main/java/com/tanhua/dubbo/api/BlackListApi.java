package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tanhua.model.domain.BlackList;
import com.tanhua.model.domain.UserInfo;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:03
 */
public interface BlackListApi {

    // 分页查询黑名单
    IPage<UserInfo> blackListByPage(Integer page, Integer pagesize, Long userId);

    // 删除黑名单
    void removeBlackList(Long userId, Long bUserId);
}
