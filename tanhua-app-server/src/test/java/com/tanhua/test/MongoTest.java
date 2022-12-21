package com.tanhua.test;

import com.tanhua.app.AppServerApplication;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.model.domain.RecommendUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 16:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class MongoTest {

    @DubboReference
    private RecommendUserApi recommendUserApi;

    @DubboReference
    private CommentApi commentApi;

    @Test
    public void testCommentApi() {
//        commentApi.hasComment()
    }

    @Test
    public void testRecommendUser() {
        RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(1L);
        System.out.println(recommendUser);
    }
}
