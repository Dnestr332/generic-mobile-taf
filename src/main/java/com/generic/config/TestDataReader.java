package com.generic.config;

public class TestDataReader {

    private TestDataReader() {}

    private static final PropertyReader testData = new PropertyReader("test-data.properties");

    /**
     * Gets a test data property value by key.
     *
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key){
        return testData.getProperty(key);
    }
}
