package com.devcci.devtoy.common.constants;

public class AuthConstants {
    private AuthConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String ROLES = "roles";
    public static final String BEARER = "Bearer ";
    public static final String TYPE = "type";
    public static final String ACCESS_TOKEN = "access";
    public static final String REFRESH_TOKEN = "refresh";
}
