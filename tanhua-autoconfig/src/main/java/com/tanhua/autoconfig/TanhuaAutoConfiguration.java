package com.tanhua.autoconfig;

import com.tanhua.autoconfig.properties.AipFaceProperties;
import com.tanhua.autoconfig.properties.HuanXinProperties;
import com.tanhua.autoconfig.properties.OssProperties;
import com.tanhua.autoconfig.properties.SmsProperties;
import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.HuanXinTemplate;
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
        AipFaceProperties.class,
        HuanXinProperties.class
})
public class TanhuaAutoConfiguration {


    // 短信发送sms
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties){
        return new SmsTemplate(smsProperties);
    }

    // 文件存储oss
    @Bean
    public OssTemplate ossTemplate(OssProperties properties){
        return new OssTemplate(properties);
    }

    // 百度人脸识别
    @Bean
    public AipFaceTemplate aipFaceTemplate(AipFaceProperties properties){
        return new AipFaceTemplate(properties);
    }

    // 环信即时通讯
    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties properties){
        return new HuanXinTemplate(properties);
    }

}
