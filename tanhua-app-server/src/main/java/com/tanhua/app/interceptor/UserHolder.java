package com.tanhua.app.interceptor;

import com.tanhua.model.domain.User;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 17:43
 */
public class UserHolder {

    private static ThreadLocal<User> t1 = new ThreadLocal<User>();

    // 设置User
    public static void set(User user){
        t1.set(user);
    }
    // 获取User
    public static User get(User user){
        return t1.get();
    }

    // 获取UserId
    public static Long getUserId(){
        return t1.get().getId();
    }

    // 获取Mobile
    public static String getMobile(){
        return t1.get().getMobile();
    }

    public static void remove() {
        t1.remove();
    }
}
