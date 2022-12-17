package com.tanhua.autoconfig;

import com.tanhua.autoconfig.properties.AipFaceProperties;
import com.tanhua.autoconfig.properties.OssProperties;
import com.tanhua.autoconfig.properties.SmsProperties;
import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OssTemplate;
import com.tanhua.autoconfig.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:15
 */
@EnableConfigurationProperties({
        SmsProperties.class,
        OssProperties.class,
        AipFaceProperties.class
})
public class TanhuaAutoConfiguration {


    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties){
        return new SmsTemplate(smsProperties);
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties properties){
        return new OssTemplate(properties);
    }

    @Bean
    public AipFaceTemplate aipFaceTemplate(AipFaceProperties properties){
        return new AipFaceTemplate(properties);
    }
}
