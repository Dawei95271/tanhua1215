package com.tanhua.model.vo;

import com.tanhua.model.domain.BasePojo;
import com.tanhua.model.domain.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo extends BasePojo {

    private Long id; //用户id
    private String nickname; //昵称
    private String avatar; //用户头像
    private String birthday; //生日
    private String gender; //性别
    private String age; //年龄
    private String city; //城市
    private String income; //收入
    private String education; //学历
    private String profession; //行业
    private Integer marriage; //婚姻状态

    public static UserInfoVo init(UserInfo info){
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(info, vo);
        // 只有age类型不同
        if(info.getAge() != null){
            vo.setAge(info.getAge().toString());
        }
        return vo;
    }
}