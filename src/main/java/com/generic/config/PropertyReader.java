package com.generic.config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class PropertyReader {

    private final Properties properties = new Properties();

    /**
     * Constructs a PropertyReader for a given resource name.
     *
     * @param resourceName the name of the properties file in the classpath
     */
    public PropertyReader(String resourceName) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourceName)) {

            if (is == null) {
                throw new IllegalStateException("Resource not found on classpath: " + resourceName);
            }
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource: " + resourceName, e);
        }
    }

    /**
     * Gets a property value by key.
     *
     * @param key the property key
     * @return the property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
