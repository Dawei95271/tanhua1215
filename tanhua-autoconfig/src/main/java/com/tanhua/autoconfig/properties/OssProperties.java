package com.tanhua.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 15:58
 */
@Data
@ConfigurationProperties(prefix="tanhua.oss")
public class OssProperties {

    private String accessKey;
    private String secret;
    private String bucketName;
    private String url; //域名
    private String endpoint;
}
