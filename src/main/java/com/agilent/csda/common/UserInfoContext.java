package com.agilent.csda.common;

import com.agilent.csda.acl.model.User;

/**
 * Save UserInfo Context
 */
public class UserInfoContext {
    private static ThreadLocal<User> userInfo = new ThreadLocal<User>();
    public static String KEY_USERINFO_IN_HTTP_HEADER = "X-AUTO-FP-USERINFO";

    public UserInfoContext() {
    }

    public static User getUser() {
        return (User) userInfo.get();
    }

    public static void setUser(User user) {
        userInfo.set(user);
    }
}