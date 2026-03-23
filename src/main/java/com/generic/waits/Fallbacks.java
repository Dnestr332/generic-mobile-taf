package com.generic.waits;

import com.generic.utils.MobileUtils;
import io.appium.java_client.AppiumDriver;
import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.generic.mobile.TestEnvironment.isAndroid;

@Slf4j
@Component
@ScenarioScope
public class Fallbacks {

    private final AppiumDriver driver;

    public Fallbacks(AppiumDriver driver) {
        this.driver = driver;
    }

    public WebElement relocate(By locator) {
        return driver.findElement(locator);
    }

    public List<WebElement> relocateList(By locator) {
        return driver.findElements(locator);
    }

    public String readText(By locator) {
        return MobileUtils.getActualText(driver.findElement(locator));
    }

    public void pause() {
        MobileUtils.hardSleep(3, "UI settling");
    }

    public void shortPause() {
        MobileUtils.hardSleep(1, "Short UI settling");
    }

    public void tapByElementCenter(By locator) {
        WebElement el = driver.findElement(locator);

        int centerX = el.getRect().getX() + el.getRect().getWidth() / 2;
        int centerY = el.getRect().getY() + el.getRect().getHeight() / 2;

        log.info("Fallback tap at center ({}, {}) for {}", centerX, centerY, locator);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), centerX, centerY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public void clickNativeByPlatform(By locator) {
        WebElement el = driver.findElement(locator);

        if(isAndroid()) {
            driver.executeScript("mobile: clickGesture", Map.of(
                    "elementId", ((RemoteWebElement) el).getId()
            ));
        } else {
            Rectangle r = el.getRect();
            driver.executeScript("mobile: tap", Map.of(
                    "x", r.getX() + r.getWidth() / 2,
                    "y", r.getY() + r.getHeight() / 2
            ));
        }
    }
}
