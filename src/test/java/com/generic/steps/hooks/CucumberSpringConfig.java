package com.generic.steps.hooks;

import com.generic.spring.MobileTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = MobileTestConfig.class)
public class CucumberSpringConfig {
}
