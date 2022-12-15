package com.tanhua.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:19
 */

@Data
@ConfigurationProperties(prefix = "tanhua.sms")
public class SmsProperties {

    private String accessKey;
    private String secret;
    private String signName;
    private String templateCode;
    private String endpoint;

}
