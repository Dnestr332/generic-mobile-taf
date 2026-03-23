package com.generic.config;

public class TestDataReader {

    private TestDataReader() {}

    private static final PropertyReader testData = new PropertyReader("test-data.properties");

    public static String getProperty(String key){
        return testData.getProperty(key);
    }
}
