package com.generic.context;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContext {

    private static Environment env;

    public SpringContext(Environment env) {
        this.env = env;
    }

    public static String getProperty(String key) {
        return env.getProperty(key);
    }
}
