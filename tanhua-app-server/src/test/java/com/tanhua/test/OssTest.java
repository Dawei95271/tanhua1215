package com.tanhua.test;

import com.tanhua.app.AppServerApplication;
import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 16:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class OssTest {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private AipFaceTemplate aipFaceTemplate;

    @Test
    public void testAipFace() {
        boolean ret = aipFaceTemplate.faceDetect("https://tanhua12151.oss-cn-hangzhou.aliyuncs.com/demo/11.jpg");
        System.out.println(ret);
    }

    @Test
    public void testOss() throws FileNotFoundException {
        InputStream is = new FileInputStream("C:\\Users\\16420\\Pictures\\1.jpg");
        String filename = "test.jpg";
        String url = ossTemplate.upload(filename, is);
        log.warn("文件的地址是：{}", url);
    }





}
