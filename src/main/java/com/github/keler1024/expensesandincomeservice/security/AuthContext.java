package com.github.keler1024.expensesandincomeservice.security;

//Temporary
public class AuthContext {

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    protected static void setUserId(Long userId) {
        if (userId == null || userId < 0) {
            throw new IllegalArgumentException("Invalid user id");
        }
        userIdHolder.set(userId);
    }

    public static Long getUserId() {
        Long userId = userIdHolder.get();
        if (userId == null) {
            throw new RuntimeException("user id requested before it was initialized");
        }
        return userId;
    }
}
