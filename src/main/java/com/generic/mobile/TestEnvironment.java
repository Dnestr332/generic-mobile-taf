package com.generic.mobile;

import com.generic.enums.Platform;

import java.util.Arrays;

public final class TestEnvironment {

    private TestEnvironment() {}

    public static Platform getPlatform() {
        String raw = System.getProperty("platform");

        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException("Platform is not defined.");
        }

        try {
            return Platform.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Unsupported platform: " + raw + ". Allowed values: " + Arrays.toString(Platform.values()));
        }
    }

    public static boolean isCi() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }

    public static boolean isAndroid() {
        return getPlatform().isAndroid();
    }

    public static boolean isIos() {
        return getPlatform().isIos();
    }
}
