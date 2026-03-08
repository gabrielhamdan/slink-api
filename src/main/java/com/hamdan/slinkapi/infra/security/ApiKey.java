package com.hamdan.slinkapi.infra.security;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.UUID;

public class ApiKey {

    public static final String X_API_KEY_HEADER = "X-API-Key";

    public static String get(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(X_API_KEY_HEADER)).orElse("");
    }

    public static String generate(String userName) {
        return UUID.fromString(userName).toString();
    }

}
