package com.tanhua.model.vo;

import com.tanhua.model.domain.RecommendUser;
import com.tanhua.model.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 17:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayBest implements Serializable {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Integer fateValue; // 缘分值
    
    // 构造TodayBest
    public static TodayBest init(UserInfo userInfo, RecommendUser recommendUser){
        TodayBest vo = new TodayBest();
        BeanUtils.copyProperties(userInfo, vo);
        // 设置tags
        String tag = userInfo.getTags();
        if(tag != null){
            vo.tags = tag.split(",");
        }
        // 设置缘分值
        vo.setFateValue(recommendUser.getScore().intValue());
        return vo;
    }


}
