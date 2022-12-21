package com.tanhua.test;

import com.tanhua.app.AppServerApplication;
import com.tanhua.autoconfig.template.HuanXinTemplate;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class HuanXinTest {
    
    @Autowired
    private HuanXinTemplate template;
    @DubboReference
    private UserApi userApi;

    // 批量注册环信账户
    @Test
    public void register() {
        List<User> userList = userApi.findAll();
        for (User user : userList) {
            Boolean create = template.createUser("hx" + user.getId(), Constants.INIT_PASSWORD);
            if(create){
                user.setHxUser("hx"+user.getId());
                user.setHxPassword(Constants.INIT_PASSWORD);
                userApi.update(user);
            }
        }
    }


    @Test
    public void test1() {
        template.createUser("user01", "123456");
    }
}
