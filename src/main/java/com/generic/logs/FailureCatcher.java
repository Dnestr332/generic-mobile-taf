package com.generic.logs;

import com.generic.context.TestFailureContext;
import com.generic.enums.Action;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@ScenarioScope
@RequiredArgsConstructor
public class FailureCatcher {

    private final PrettyPrinter prettyPrinter;

    /**
     * Executes a supplier with failure capture.
     *
     * @param action   the action being performed
     * @param locator  the locator involved
     * @param details  additional details
     * @param supplier the supplier to execute
     * @param <T>      the return type
     * @return the result of the supplier
     */
    public <T> T withFailureCapture(Action action, By locator, String details, Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        prettyPrinter.start(action, locator, details);

        try {
            T result = supplier.get();
            long current = System.currentTimeMillis() - start;
            double sec = (double) current / 1000;
            prettyPrinter.ok(sec);
            return result;
        } catch (Throwable t) {
            prettyPrinter.fail(t);
            TestFailureContext.setError(t);
            throw t;
        }
    }

    /**
     * Executes a supplier with failure capture.
     *
     * @param action   the action being performed
     * @param locator  the locator involved
     * @param supplier the supplier to execute
     * @param <T>      the return type
     * @return the result of the supplier
     */
    public <T> T withFailureCapture(Action action, By locator, Supplier<T> supplier) {
        return withFailureCapture(action, locator, null, supplier);
    }

    /**
     * Executes a runnable with failure capture.
     *
     * @param action   the action being performed
     * @param locator  the locator involved
     * @param details  additional details
     * @param runnable the runnable to execute
     */
    public void withFailureCapture(Action action, By locator, String details, Runnable runnable) {
        withFailureCapture(action, locator, null, () -> {
            runnable.run();
            return null;
        });
    }
}
