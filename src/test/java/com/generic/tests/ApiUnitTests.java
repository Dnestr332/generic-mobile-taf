package com.generic.tests;

import com.generic.spring.MobileTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@Slf4j
@ContextConfiguration(classes = MobileTestConfig.class)
public class ApiUnitTests extends AbstractTestNGSpringContextTests {


}
