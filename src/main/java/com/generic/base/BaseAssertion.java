package com.generic.base;

import com.generic.assertions.Hardly;
import com.generic.assertions.Softly;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public abstract class BaseAssertion {

    /**
     * Asserts that a condition is true (hard assertion).
     *
     * @param condition the condition to check
     * @param context   the context message
     */
    public void hardlyCondition(BooleanSupplier condition, String context) {
        Hardly.isTrue(condition.getAsBoolean(), context);
    }

    /**
     * Asserts that two objects are equal (hard assertion).
     *
     * @param actual   the actual value supplier
     * @param expected the expected value
     * @param context  the context message
     * @param <T>      the type of the objects
     */
    public <T> void hardlyEquals(Supplier<T> actual, T expected, String context) {
        Hardly.isEqual(actual.get(), expected, context);
    }

    /**
     * Asserts that two doubles are equal with offset (soft assertion).
     *
     * @param actual   the actual double value supplier
     * @param expected the expected double value
     * @param offset   the allowed offset
     * @param context  the context message
     */
    public void softlyDoubleEquals(Supplier<Double> actual, double expected, double offset, String context) {
        Softly.isDoubleEqual(actual.get(), expected, offset, context);
    }

    /**
     * Asserts that a condition is true (soft assertion).
     *
     * @param condition the condition to check
     * @param context   the context message
     */
    public void softlyCondition(BooleanSupplier condition, String context) {
        boolean result = false;
        try {
            result = condition.getAsBoolean();
        } catch (WebDriverException e) {
            logSoftFail(e);
        }
        Softly.isTrue(result, context);
    }

    /**
     * Asserts that two objects are equal (soft assertion).
     *
     * @param actual   the actual value supplier
     * @param expected the expected value
     * @param context  the context message
     * @param <T>      the type of the objects
     */
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
