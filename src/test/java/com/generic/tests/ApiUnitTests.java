package com.generic.tests;

import com.generic.api.ApiClient;
import com.generic.spring.MobileTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@ContextConfiguration(classes = MobileTestConfig.class)
public class ApiUnitTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private ApiClient apiClient;

    @Test
    public void testApi() {
        log.info("testApi");
    }
}
