package com.tanhua.autoconfig.template;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.tanhua.autoconfig.properties.SmsProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:14
 */
public class SmsTemplate {

    private SmsProperties smsProperties;

    public SmsTemplate(SmsProperties smsProperties){
        this.smsProperties = smsProperties;
    }

    public void sendSms(String mobile, String code) {
        Config config = new Config()
                .setAccessKeyId(smsProperties.getAccessKey())
                .setAccessKeySecret(smsProperties.getSecret())
                .setEndpoint(smsProperties.getEndpoint());

        try {
            Client client = new Client(config);

            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName(smsProperties.getSignName())
                    .setTemplateCode(smsProperties.getTemplateCode()) //SMS_154950909
                    .setPhoneNumbers(mobile)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
