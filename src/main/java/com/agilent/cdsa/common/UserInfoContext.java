package com.agilent.cdsa.common;

import com.agilent.cdsa.model.User;

/**
 * Save UserInfo Context
 */
public class UserInfoContext {
    private static ThreadLocal<User> userInfo = new ThreadLocal<User>();

    public UserInfoContext() {
    }

    public static User getUser() {
        return (User) userInfo.get();
    }

    public static void setUser(User user) {
        userInfo.set(user);
    }
}