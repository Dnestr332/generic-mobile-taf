package com.generic.base;

import com.generic.assertions.Hardly;
import com.generic.assertions.Softly;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public abstract class BaseAssertion {

    public void hardlyCondition(BooleanSupplier condition, String context) {
        Hardly.isTrue(condition.getAsBoolean(), context);
    }

    public <T> void hardlyEquals(Supplier<T> actual, T expected, String context) {
        Hardly.isEqual(actual.get(), expected, context);
    }

    public void softlyDoubleEquals(Supplier<Double> actual, double expected, double offset, String context) {
        Softly.isDoubleEqual(actual.get(), expected, offset, context);
    }

    public void softlyCondition(BooleanSupplier condition, String context) {
        boolean result = false;
        try {
            result = condition.getAsBoolean();
        } catch (WebDriverException e) {
            logSoftFail(e);
        }
        Softly.isTrue(result, context);
    }

    public <T> void softlyEquals(Supplier<T> actual, T expected, String context) {
        T actualValue = null;
        try {
            actualValue = actual.get();
        } catch (WebDriverException e) {
            logSoftFail(e);
        }
        Softly.isEqual(actualValue, expected, context);
    }

    private void logSoftFail(RuntimeException e) {
        log.warn("Exception was caught during SOFT assertion: {}", e.getMessage());
    }
}
