// This file is auto-generated, don't edit it. Thanks.

package com.tanhua.test;
import com.aliyun.dysmsapi20170525.Client;

import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;

import javax.xml.ws.Endpoint;

/**
 * ali message sample
 */
public class Sample {


    public static void main(String[] args_) throws Exception {
        String accessKeyId =  "LTAI5tBSxBRbzhXAkS8G6SmS";
        String accessKeySecret = "4YoUATEzLrTL4nt9CdLwGoFF8GDLuF";
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint("dysmsapi.aliyuncs.com");
        Client client = new Client(config);

        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers("17854257855")
                .setTemplateParam("{\"code\":\"1234\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}