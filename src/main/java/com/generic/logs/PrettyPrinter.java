package com.generic.logs;

import com.generic.enums.Action;
import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.generic.logs.LogStyles.*;

@Slf4j
@Component
@ScenarioScope
public class PrettyPrinter {

    /**
     * Logs the start of an action.
     *
     * @param action  the action performed
     * @param locator the locator involved
     * @param details additional details
     */
    public void start(Action action, By locator, String details) {
        String prefix = BLUE + CLICK + RESET + " ";
        String shortTag = INFO_SHORT;

        String actionLabel = (details == null)
                ? action.name()
                : action.name() + " " + details;

        if (locator == null) {
            log.info("{} {} {}{}{}",
                    prefix,
                    shortTag,
                    GREEN,
                    actionLabel,
                    RESET
            );
            return;
        }

        log.info("{} {} {}{}{}  [{}] {}",
                prefix,
                shortTag,
                GREEN,
                actionLabel,
                RESET,
                getLocatorType(locator),
                getLocatorValue(locator)
        );
    }

    /**
     * Logs the successful completion of an action.
     *
     * @param sec the duration in seconds
     */
    public void ok(double sec) {
        log.info("   {} {} {} ({} sec)",
                OK_SHORT,
                GREEN + OK + RESET,
                GREEN + "OK" + RESET,
                sec
        );
    }

    /**
     * Logs an action failure.
     *
     * @param t the throwable cause
     */
    public void fail(Throwable t) {
        String line = (t.getMessage() == null)
                ? "<no message>"
                : t.getMessage().split("\\R", 2)[0];

        log.error("   {} {} {}",
                FAIL_SHORT,
                RED + FAIL + RESET,
                line
        );

        int idx = line.indexOf("By.");
        if (idx > -1) {
            log.error("     {}{}{}", RED, line.substring(idx).trim(), RESET);
        }

        StackTraceElement[] st = t.getStackTrace();
        for (int i = 0; i < Math.min(3, st.length); i++) {
            log.error("     {}{}{}", RED, st[i], RESET);
        }
    }

    private String getLocatorType(By locator) {
        String s = locator.toString();
        int separator = s.indexOf(": ");
        return (separator > -1)
                ? s.substring(0, separator)
                : s;
    }

    private static String getLocatorValue(By locator) {
        String s = locator.toString();
        int separator = s.indexOf(": ");
        return (separator > -1)
                ? s.substring(separator + 2)
                : s;
    }
}
