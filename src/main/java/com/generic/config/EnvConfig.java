package com.generic.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Environment variable reader based on {@code .env}.
 * <p>
 * Uses {@link io.github.cdimascio.dotenv.Dotenv} to load environment-specific
 * values (staging, release, API keys, database credentials, etc.).
 */
public final class EnvConfig {

    private EnvConfig() {}

    /**
     * Dotenv instance that loads environment variables (ignores if missing).
     */
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    /**
     * Reads a configuration value from system property, environment variable, or .env file.
     *
     * @param key the configuration key
     * @return the configuration value, or null if not found
     */
    private static String read(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }

        value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }

        value = DOTENV.get(key);
        if (value != null && !value.isBlank()) {
            return value;
        }

        return null;
    }
}
