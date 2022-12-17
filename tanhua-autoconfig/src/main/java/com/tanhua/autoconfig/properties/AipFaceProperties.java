package com.tanhua.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 16:42
 */
@Data
@ConfigurationProperties("tanhua.aip")
public class AipFaceProperties {

    private String url;
    private String clientId;
    private String clientSecret;
    private String authHost;

}
