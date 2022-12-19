package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Settings;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:03
 */
public interface SettingApi {

    // 根据userId查询
    Settings findByUserId(Long userId);

    // 保存settings
    void save(Settings settings);

    // 更新settings
    void update(Settings settings);
}
