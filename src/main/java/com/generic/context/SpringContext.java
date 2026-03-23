package com.generic.context;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContext {

    private static Environment env;

    /**
     * Constructs the SpringContext with the Spring Environment.
     *
     * @param env the Spring environment
     */
    public SpringContext(Environment env) {
        this.env = env;
    }

    /**
     * Gets a property from the Spring Environment.
     *
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        return env.getProperty(key);
    }
}
