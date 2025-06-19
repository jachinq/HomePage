package com.home.homepage.utils;

import com.home.homepage.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jachin
 * @since 25-06-19 11:38
 */
@Slf4j
public class RequestContext {
    // 临时数据存储
    private static Map<String, Object> m_tmpDataMap = new TreeMap<>();
    private static final ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();

    public static <T> void setTmpData(String key, T value) {
        m_tmpDataMap.put(key, value);
    }

    public static <T> T getTmpData(String key) {
        @SuppressWarnings("unchecked")
        T value = (T) m_tmpDataMap.get(key);
        return value;
    }


    public static void setUser(User user) {
        Long threadId = Thread.currentThread().getId();
        userMap.put(threadId, user);
        log.info("set user {} for thread {}", user, threadId);
    }

    public static User getUser() {
        Long threadId = Thread.currentThread().getId();
        User user = userMap.get(threadId);
        log.info("get user {} for thread {}", user, threadId);
        return user;
    }
}
