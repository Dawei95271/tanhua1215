package com.tanhua.test;

import com.tanhua.app.AppServerApplication;
import com.tanhua.autoconfig.template.SmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:32
 */

@SpringBootTest(classes = AppServerApplication.class)
@RunWith(SpringRunner.class)
public class SmsTest {

    @Autowired
    private SmsTemplate smsTemplate;

    @Test
    public void testSendSms() {

        smsTemplate.sendSms("17854257855", "123456");
    }
}
