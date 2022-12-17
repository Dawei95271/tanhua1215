package com.tanhua.autoconfig.template;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.tanhua.autoconfig.properties.OssProperties;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 15:59
 */
public class OssTemplate {

    private OssProperties ossProperties;

    public OssTemplate(OssProperties ossProperties){
        this.ossProperties = ossProperties;
    }

    /**
     * 文件上传
     */
    public String upload(String filename, InputStream is){
        // 文件名
        filename = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/"
                + UUID.randomUUID().toString().replaceAll("_", "-")
                + filename.substring(filename.lastIndexOf("."));

        System.out.println(filename);

        // ossClient
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecret());
        // 上传
        ossClient.putObject(ossProperties.getBucketName(), filename, is);
        ossClient.shutdown();

        String url = ossProperties.getUrl() + filename;
        return url;


    }

}
