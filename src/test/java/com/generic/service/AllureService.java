package com.generic.service;

import com.generic.utils.MobileUtils;
import io.appium.java_client.AppiumDriver;
import io.cucumber.spring.ScenarioScope;
import io.qameta.allure.Allure;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@ScenarioScope
@RequiredArgsConstructor
public class AllureService {

    private final AppiumDriver driver;

    public void attachScreenshot() {
        if (driver == null) return;

        WebDriver realDriver = MobileUtils.unwrap(driver);
        if (!(realDriver instanceof TakesScreenshot)) return;

        byte[] screenshot = ((TakesScreenshot) realDriver)
                .getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment("Screenshot",
                new ByteArrayInputStream(screenshot)
        );
    }
}
