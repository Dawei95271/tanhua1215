package com.tanhua.app.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tanhua.app.exception.BusinessException;
import com.tanhua.app.interceptor.UserHolder;
import com.tanhua.autoconfig.template.OssTemplate;
import com.tanhua.commons.enums.ErrorResult;
import com.tanhua.commons.utils.Constants;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.model.domain.UserInfo;
import com.tanhua.model.mongo.Movement;
import com.tanhua.model.vo.MovementsVo;
import com.tanhua.model.vo.PageResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/20 17:25
 */
@Service
public class MovementService {

    @Autowired
    private OssTemplate ossTemplate;
    @DubboReference
    private MovementApi movementApi;
    @DubboReference
    private UserInfoApi userInfoApi;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public MovementsVo findById(String movementId) {
        Movement movement = movementApi.findById(movementId);
        if(movement == null){
            return null;
        }
        UserInfo info = userInfoApi.findById(movement.getUserId());
        return MovementsVo.init(info, movement);
    }

    public PageResult recommend(Integer page, Integer pagesize) {
        String redisKey = Constants.MOVEMENTS_RECOMMEND + UserHolder.getUserId();
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        List<Movement> list = Collections.emptyList();
        // 不存在，则随机推荐
        if(StrUtil.isEmpty(redisValue)){
            list = movementApi.randomMovement(pagesize);
        } else{
            String[] values = redisValue.split(",");
            if((page - 1) * pagesize < values.length){
                // 可以分页查询
                List<Long> pids = Arrays.stream(values).skip((page - 1) * pagesize).limit(pagesize)
                        .map(e -> Long.parseLong(e))
                        .collect(Collectors.toList());
                list = movementApi.findMovementByPids(pids);
            }
        }
        return getPageResult(page, pagesize, list);
    }

    // 通过movement，构造pageResult
    private PageResult getPageResult(Integer page, Integer pagesize, List<Movement> list){
        List<Long> userIds = CollUtil.getFieldValues(list, "userId", Long.class);
        Map<Long, UserInfo> infos = userInfoApi.findByIds(userIds, null);
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement movement : list) {
            UserInfo userInfo = infos.get(movement.getUserId());
            if(userInfo != null){
                MovementsVo vo = MovementsVo.init(userInfo, movement);
                vos.add(vo);
            }
        }
        return new PageResult(page, pagesize, 0l, vos);
    }

    public PageResult findFriendMovement(Integer page, Integer pagesize) {
        PageResult pr = movementApi.findFriendMovement(UserHolder.getUserId(), page, pagesize);
        List<Movement> items = (List<Movement>) pr.getItems();
        if(CollUtil.isEmpty(items)){
            return pr;
        }
        // 好友的id集合
        List<Long> ids = CollUtil.getFieldValues(items, "userId", Long.class);
        Map<Long, UserInfo> infoList = userInfoApi.findByIds(ids, null);
        // 构建返回值
        List<MovementsVo> vos = new ArrayList<>();
        for (Movement item : items) {
            UserInfo userInfo = infoList.get(item.getUserId());
            if(userInfo != null){
                MovementsVo vo = MovementsVo.init(userInfo, item);
                vos.add(vo);
            }

        }
        pr.setItems(vos);
        return pr;

    }

    public PageResult findByUserId(Long userId, Integer page, Integer pagesize) {
        PageResult pr = movementApi.findByUserId(userId, page, pagesize);
        List<Movement> items = (List<Movement>) pr.getItems();
        if(CollUtil.isEmpty(items)){
            return pr;
        }
        UserInfo userInfo = userInfoApi.findById(userId);
        List<MovementsVo> list = new ArrayList<>();
        for (Movement item : items) {
            MovementsVo vo = MovementsVo.init(userInfo, item);
            list.add(vo);
        }
        pr.setItems(list);
        return pr;
    }

    public void publishMovement(Movement movement, MultipartFile[] imageContent) throws IOException {
        // 动态必须有内容
        if(StrUtil.isEmpty(movement.getTextContent())){
            throw new BusinessException(ErrorResult.contentError());
        }
        // 图片处理，上传
        List<String> urlList = new ArrayList<>();
        for (MultipartFile image : imageContent) {
            String url = ossTemplate.upload(image.getOriginalFilename(), image.getInputStream());
            urlList.add(url);
        }
        // 发布动态
        movement.setUserId(UserHolder.getUserId());
        movement.setMedias(urlList);
        movementApi.publish(movement);
    }



}
