package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.autoconfig.template.OssTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.mongo.Video;
import com.tanhua.model.vo.PageResult;
import com.tanhua.model.vo.VideoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/23 11:26
 */
@Service
public class SmallVideosService {

    @Autowired
    private OssTemplate ossTemplate;
    @Autowired
    private FastFileStorageClient client;
    @Autowired
    private FdfsWebServer webServer;
    @DubboReference
    private VideoApi videoApi;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @DubboReference
    private UserInfoApi userInfoApi;

    // 保存小视频
    public void saveVideos(MultipartFile videoThumbnail, MultipartFile videoFile) throws IOException {
        // 上传图片
        String imageUrl = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
        // 上传视频
        String filename = videoFile.getOriginalFilename();
        // 获取扩展名
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        StorePath path = client.uploadFile(videoFile.getInputStream(), videoFile.getSize(), extName, null);
        String videoUrl = webServer.getWebServerUrl() + path.getFullPath();
        // 保存到数据库
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        video.setUserId(UserHolder.getUserId());
        video.setText("首页");
        video.setPicUrl(imageUrl);
        String videoId = videoApi.save(video);
        if(StrUtil.isEmpty(videoId)){
            // 保存失败，则抛出异常
            throw new BusinessException(ErrorResult.error());
        }

    }

    // 分页查询小视频，优先推荐视频，再是视频库中随机视频
    @Cacheable(
            value = "videos",  // videos::1_1_10
            key = "T(com.tanhua.app.interceptor.UserHolder).getUserId() + '_' + #page + '_' + #pagesize"
    )
    public PageResult findVideos(Integer page, Integer pagesize) {
        // redis中推荐数据
        String key = Constants.VIDEOS_RECOMMEND + UserHolder.getUserId();
        String value = redisTemplate.opsForValue().get(key);
        // value存在，则从redis中取推荐id
        List<Video> list = new ArrayList<>();
        int redisPage = 0;
        if(StrUtil.isNotEmpty(value)){
            String[] recommendIds = value.split(",");
            if((page - 1) * pagesize < recommendIds.length){
                // 获取pagsize长度的ids
                List<Long> vids = Arrays.stream(recommendIds).skip((page - 1) * pagesize).limit(pagesize)
                        .map(e -> Long.valueOf(e))
                        .collect(Collectors.toList());
                list = videoApi.queryVideosByVids(vids);
            }
            redisPage = PageUtil.totalPage(recommendIds.length, pagesize);
        }
        if(list.isEmpty()){
            // 没有优先推荐，则随机推荐
            list = videoApi.queryVideos(page - redisPage, pagesize);
        }
        // 构造vo返回
        List<Long> ids = CollUtil.getFieldValues(list, "userId", Long.class);
        Map<Long, UserInfo> infoMap = userInfoApi.findByIds(ids, null);
        List<VideoVo> vos = new ArrayList<>();
        for (Video video : list) {
            UserInfo userInfo = infoMap.get(video.getUserId());
            if (userInfo != null){
                VideoVo vo = VideoVo.init(userInfo, video);
                vos.add(vo);
            }
        }
        return new PageResult(page, pagesize, 0l, vos);
    }

























}
