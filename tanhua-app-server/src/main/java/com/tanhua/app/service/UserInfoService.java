package com.tanhua.app.service;

import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.autoconfig.template.AipFaceTemplate;
import com.tanhua.autoconfig.template.OssTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 17:53
 */
@Service
public class UserInfoService {

    @DubboReference
    private UserInfoApi userInfoApi;
    @Autowired
    private AipFaceTemplate aipFaceTemplate;
    @Autowired
    private OssTemplate ossTemplate;

    public void head(MultipartFile headPhoto) {
        String imageUrl = null;
        try {
            imageUrl = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean ret = aipFaceTemplate.faceDetect(imageUrl);
        if(! ret){
            throw new BusinessException(ErrorResult.faceError());
        }
        // 图片正确
        UserInfo userInfo = new UserInfo();
        userInfo.setId(UserHolder.getUserId());
        userInfo.setAvatar(imageUrl);
        // 更新
        userInfoApi.updateById(userInfo);

    }

    public void loginReginfo(UserInfo userInfo) {
        userInfoApi.save(userInfo);
    }













}
