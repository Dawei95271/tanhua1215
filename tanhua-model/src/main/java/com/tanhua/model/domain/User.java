package com.tanhua.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/16 17:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BasePojo {

    private Long id;
    private String mobile;
    private String password;

    private String hxUser;
    private String hxPassword;

}
