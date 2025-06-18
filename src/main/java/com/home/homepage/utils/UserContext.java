package com.home.homepage.utils;

import com.home.homepage.entity.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Jachin
 * @since 25-06-17 14:53
 */
public class UserContext {
    private static final ConcurrentHashMap<String, User> THREAD_LOCAL = new ConcurrentHashMap<>();

    public static void setUser(String sessionId, User user) {
        if (sessionId == null || sessionId.isEmpty()) {
            System.out.println("sessionId is null or empty");
            return;
        }
        if (user == null) {
            System.out.println("user is null");
            return;
        }
        THREAD_LOCAL.put(sessionId, user);
        System.out.println("set user map=" + THREAD_LOCAL);
    }
    public static User getUser(String sessionId) {
        System.out.println("get user map=" + THREAD_LOCAL);
        return THREAD_LOCAL.get(sessionId);
    }
    public static void clear(String sessionId) {
        THREAD_LOCAL.remove(sessionId); // 请求结束后需清理，避免内存泄漏
    }
}
