package com.generic.mobile;

import com.generic.enums.Platform;

import java.util.Arrays;

public final class TestEnvironment {

    private TestEnvironment() {}

    /**
     * @return the current platform defined in system properties
     */
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

    /**
     * @return true if the current environment is CI
     */
    public static boolean isCi() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }

    /**
     * @return true if the platform is Android
     */
    public static boolean isAndroid() {
        return getPlatform().isAndroid();
    }

    /**
     * @return true if the platform is iOS
     */
    public static boolean isIos() {
        return getPlatform().isIos();
    }
}
