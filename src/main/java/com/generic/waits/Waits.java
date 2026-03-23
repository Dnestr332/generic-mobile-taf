package com.generic.waits;

import com.generic.mobile.TestEnvironment;
import io.appium.java_client.AppiumDriver;
import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

@Component
@ScenarioScope
public class Waits {

    private final AppiumDriver driver;
    private final long longTimeout;
    private final long shortTimeout;
    private final long polling;

    public Waits(
            AppiumDriver driver,
            @Value("${wait.local.long}") long localLong,
            @Value("${wait.ci.long}") long ciLong,
            @Value("${wait.local.short}") long localShort,
            @Value("${wait.ci.short}") long ciShort,
            @Value("${wait.polling.millis}") long polling
    ) {
        boolean isCi = TestEnvironment.isCi();

        this.driver = driver;
        this.longTimeout = isCi ? ciLong : localLong;
        this.shortTimeout = isCi ? ciShort : localShort;
        this.polling = polling;
    }

    private FluentWait<AppiumDriver> getWait(long timeout) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(polling))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
    }

    //region ELEMENT WAITS
    public WebElement visible(By locator) {
        return getWait(longTimeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement visibleShort(By locator) {
        return getWait(shortTimeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> visibleList(By locator) {
        return getWait(longTimeout)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public WebElement clickable(By locator) {
        return getWait(longTimeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement clickableShort(By locator) {
        return getWait(shortTimeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
    //endregion

    //region FUNCTIONAL WAITS
    public void until(BooleanSupplier condition) {
        getWait(longTimeout).until(driver -> condition.getAsBoolean());
    }

    public boolean untilReturn(BooleanSupplier condition) {
        return getWait(longTimeout).until(driver -> condition.getAsBoolean());
    }

    public void attributeContains(By locator, String attribute, String value) {
        getWait(longTimeout).until(driver ->
                Objects.requireNonNull(driver
                                .findElement(locator)
                                .getAttribute(attribute))
                        .contains(value)
        );
    }
    //endregion

    //region SAFE CHECKERS
    public boolean isVisible(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isQuickVisible(By locator) {
        try {
            return visibleShort(locator) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClickable(By locator) {
        try {
            return clickableShort(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    //endregion
}
