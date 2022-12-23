package com.tanhua.app.service;

import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.dubbo.api.UserLocationApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/22 15:58
 */
@Service
public class BaiduService {

    @DubboReference
    private UserLocationApi userLocationApi;

    // 上报地理位置
    public void updateLocation(Double longitude, Double latitude, String address) {
        Boolean ret = userLocationApi.updateLocation(UserHolder.getUserId(), longitude, latitude, address);
        if(! ret){
            throw new BusinessException(ErrorResult.error());
        }
    }
}
