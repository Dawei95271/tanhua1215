package com.tanhua.autoconfig;

import com.tanhua.autoconfig.properties.SmsProperties;
import com.tanhua.autoconfig.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:15
 */
@EnableConfigurationProperties({
        SmsProperties.class
})
public class TanhuaAutoConfiguration {


    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties){
        return new SmsTemplate(smsProperties);
    }
}
