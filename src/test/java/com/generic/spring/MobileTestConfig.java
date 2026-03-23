package com.generic.spring;

import com.generic.mobile.MobileDriverFactory;
import com.generic.mobile.TestEnvironment;
import io.appium.java_client.AppiumDriver;
import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
        "com.generic.context",
        "com.generic.logs",
        "com.generic.mobile",
        "com.generic.waits",
        "com.generic.service",
        "com.generic.pages",
        "com.generic.resolvers",
        "com.generic.api",
        "com.generic.flows",
        "com.generic.assertions"
})
public class MobileTestConfig {

    @Bean(destroyMethod = "quit")
    @ScenarioScope
    public AppiumDriver driver(MobileDriverFactory factory) {
        return factory.createDriver();
    }
}
