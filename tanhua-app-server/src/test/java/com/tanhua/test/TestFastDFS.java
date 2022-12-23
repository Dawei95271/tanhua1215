package com.tanhua.test;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.app.AppServerApplication;
import com.tanhua.autoconfig.template.HuanXinTemplate;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/21 18:04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class TestFastDFS {

    @Autowired
    private FastFileStorageClient client;
    @Autowired
    private FdfsWebServer webServer;

    @Test
    public void test1() throws FileNotFoundException {
        File file = new File("C:\\Users\\16420\\Pictures\\1.jpg");
        StorePath path = client.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
        String url = webServer.getWebServerUrl() + path.getFullPath();
        System.out.println(url);
    }
}
