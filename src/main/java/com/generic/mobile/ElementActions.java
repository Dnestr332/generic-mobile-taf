package com.generic.mobile;

import com.generic.enums.Action;
import com.generic.enums.Strategy;
import com.generic.logs.FailureCatcher;
import com.generic.utils.MobileUtils;
import com.generic.waits.Fallbacks;
import com.generic.waits.Waits;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.generic.enums.Action.*;
import static com.generic.enums.Strategy.*;
import static com.generic.utils.MobileUtils.getActualText;
import static com.generic.utils.MobileUtils.updateValue;

@Slf4j
@Component
@ScenarioScope
@RequiredArgsConstructor
public class ElementActions {

    private Waits waits;
    public final Fallbacks fallbacks;
    private final FailureCatcher failureCatcher;

    //region FIND ELEMENT
    /**
     * Finds a web element with a specific strategy.
     *
     * @param locator  the locator
     * @param strategy the wait strategy
     * @return the found web element
     */
    public WebElement find(By locator, Strategy strategy) {
        return failureCatcher.withFailureCapture(FIND, locator,
                () -> {
                    switch (strategy) {
                        case HARD_WAIT -> {
                            logFallback(FIND, HARD_WAIT, locator);
                            fallbacks.pause();
                            return fallbacks.relocate(locator);
                        }
                        case DEFAULT -> {
                            return waits.visible(locator);
                        }
                    }
                    throw new NoSuchElementException("Element is not found using locator: " + locator);
                }
        );
    }

    /**
     * Finds a web element with default strategy.
     *
     * @param locator the locator
     * @return the found web element
     */
    public WebElement find(By locator) {
        return find(locator, DEFAULT);
    }
    //endregion

    //region FIND LIST
    /**
     * Finds a list of web elements with a specific strategy.
     *
     * @param locator  the locator
     * @param strategy the wait strategy
     * @return the list of found web elements
     */
    public List<WebElement> findList(By locator, Strategy strategy) {
        return failureCatcher.withFailureCapture(FIND_LIST, locator,
                () -> {
                    switch (strategy) {
                        case HARD_WAIT -> {
                            logFallback(FIND_LIST, HARD_WAIT, locator);
                            fallbacks.pause();
                            return fallbacks.relocateList(locator);
                        }
                        case DEFAULT -> {
                            List<WebElement> list = waits.visibleList(locator);
                            if (!list.isEmpty()) {
                                return list;
                            }
                        }
                    }
                    return List.of();
                }
        );
    }

    /**
     * Finds a list of web elements with default strategy.
     *
     * @param locator the locator
     * @return the list of found web elements
     */
    public List<WebElement> findList(By locator) {
        return findList(locator, DEFAULT);
    }
    //endregion

    //region CLICK/TAP
    /**
     * Clicks on an element with a specific strategy.
     *
     * @param locator  the locator
     * @param strategy the wait strategy
     */
    public void click(By locator, Strategy strategy) {
        failureCatcher.withFailureCapture(CLICK, locator, null, () -> {
                    switch (strategy) {
                        case HARD_WAIT -> {
                            logFallback(CLICK_BY_NATIVE, HARD_WAIT, locator);
                            fallbacks.pause();
                            clickNative(locator);
                        }
                        case FAST_TRY -> {
                            try {
                                waits.clickableShort(locator).click();
                            } catch (Exception e) {
                                logFallback(CLICK_BY_NATIVE, NO_WAIT, locator);
                                clickNative(locator);
                            }
                        }
                        case DEFAULT -> {
                            try {
                                waits.clickable(locator).click();
                            } catch (Exception e) {
                                logFallback(CLICK, NO_WAIT, locator);
                                clickNative(locator);
                            }
                        }
                    }
                }
        );
    }

    /**
     * Clicks on an element with default strategy.
     *
     * @param locator the locator
     */
    public void click(By locator) {
        click(locator, DEFAULT);
    }

    /**
     * Clicks on an element quickly without long waits.
     *
     * @param locator the locator
     */
    public void fastClick(By locator) {
        click(locator, FAST_TRY);
    }

    /**
     * Clicks on an element using native mobile click (Appium).
     *
     * @param locator the locator
     */
    public void clickNative(By locator) {
        failureCatcher.withFailureCapture(CLICK_BY_NATIVE, locator, null, () -> {
            try {
                fallbacks.clickNativeByPlatform(locator);
            } catch (Exception e) {
                log.warn("Native click failed. Trying coordinate tap fallback.");
                fallbacks.tapByElementCenter(locator);
            }
        });
    }
    //endregion

    //region TYPE & GET TEXT
    /**
     * Types text into an element.
     *
     * @param locator the locator
     * @param text    the text to type
     */
    public void type(By locator, String text) {
        failureCatcher.withFailureCapture(
                TYPE, locator, "'" + text + "'",
                () -> updateValue(waits.clickable(locator), text)
        );
    }

    /**
     * Gets text from an element with a specific strategy.
     *
     * @param locator  the locator
     * @param strategy the wait strategy
     * @return the element text
     */
    public String text(By locator, Strategy strategy) {
        return failureCatcher.withFailureCapture(GET_TEXT, locator, () -> {
                    switch (strategy) {
                        case HARD_WAIT -> {
                            logFallback(GET_TEXT, HARD_WAIT, locator);
                            fallbacks.pause();
                            return fallbacks.readText(locator);
                        }
                        case DEFAULT -> {
                            for (int i = 0; i < 3; i++) {
                                try {
                                    return getActualText(waits.visible(locator));
                                } catch (StaleElementReferenceException e) {
                                    log.debug("Text stale (attempt {}), retrying: {}", i + 1, locator);
                                    fallbacks.shortPause();
                                }
                            }
                            log.warn("⚠ TEXT fallback used after retries for {}", locator);
                            return fallbacks.readText(locator);
                        }
                    }
                    return "";
                }
        );
    }

    /**
     * Gets text from an element with default strategy.
     *
     * @param locator the locator
     * @return the element text
     */
    public String text(By locator) {
        return text(locator, DEFAULT);
    }
    //endregion

    //region CHECKERS
    /**
     * Checks if an element is visible.
     *
     * @param locator the locator
     * @return true if visible, false otherwise
     */
    public boolean isVisible(By locator) {
        try {
            return waits.visible(locator).isDisplayed();
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException ignored) {
            return false;
        }
    }

    /**
     * Checks if an element is visible quickly.
     *
     * @param locator the locator
     * @return true if visible, false otherwise
     */
    public boolean isQuickVisible(By locator) {
        try {
            WebElement el = waits.visibleShort(locator);
            return el != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Checks if an element is enabled.
     *
     * @param locator the locator
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled(By locator) {
        try {
            WebElement el = waits.visibleShort(locator);
            return MobileUtils.isButtonEnabled(el);
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException ignored) {
            return false;
        }
    }
    //endregion

    private void logFallback(Action action, Strategy strategy, By locator) {
        log.warn("⚠ {} fallback used [{}] for {}", action, strategy, locator);
    }
}
