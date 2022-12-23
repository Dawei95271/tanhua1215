package com.tanhua.test;

import com.tanhua.app.AppServerApplication;
import com.tanhua.dubbo.api.UserLocationApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class TestUserLocationApi {

    @DubboReference
    private UserLocationApi userLocationApi;

    @Test
    public void testUpdateUserLocation() {
        this.userLocationApi.updateLocation(1L, 32.020711,118.78883, "南京夫子庙");
        this.userLocationApi.updateLocation(2L, 32.072163,118.798823, "玄武湖公园");
        this.userLocationApi.updateLocation(3L, 31.952568,118.756552, "翠玲银河");
        this.userLocationApi.updateLocation(4L, 31.953874,118.753987, "三江学院");
        this.userLocationApi.updateLocation(5L, 31.939639,118.744975, "软件谷");
        this.userLocationApi.updateLocation(6L, 31.956273,118.764219, "银杏山庄");
        this.userLocationApi.updateLocation(7L, 31.974781,118.764322, "中国移动");
        this.userLocationApi.updateLocation(8L, 131.973468,118.76209, "战争博物馆");
        this.userLocationApi.updateLocation(9L, 31.948331,118.739717, "润川驾校");
        this.userLocationApi.updateLocation(10L, 116.333374,40.009645, "清华大学");
        this.userLocationApi.updateLocation(41L, 116.316833,39.998877, "北京大学");
        this.userLocationApi.updateLocation(42L, 117.180115,39.116464, "天津大学(卫津路校区)");
    }
}