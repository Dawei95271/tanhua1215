package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.model.domain.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserInfoMapper extends BaseMapper<UserInfo> {

    // 分页查询
    @Select("SELECT ui.id, ui.avatar, ui.nickname, ui.gender, ui.age \n" +
            "FROM `tb_black_list` bl\n" +
            "INNER JOIN tb_user_info ui\n" +
            "ON bl.black_user_id = ui.id\n" +
            "WHERE bl.user_id = #{userId}")
    IPage<UserInfo> findBlackListByPage(@Param("pages") Page pages,@Param("userId") Long userId);
}
