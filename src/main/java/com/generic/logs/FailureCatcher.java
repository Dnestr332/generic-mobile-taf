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

    public <T> T withFailureCapture(Action action, By locator, Supplier<T> supplier) {
        return withFailureCapture(action, locator, null, supplier);
    }

    public void withFailureCapture(Action action, By locator, String details, Runnable runnable) {
        withFailureCapture(action, locator, null, () -> {
            runnable.run();
            return null;
        });
    }
}
