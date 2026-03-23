package com.generic.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.generic.logs.LogStyles.*;
import static com.generic.mobile.TestEnvironment.isIos;

/**
 * Utility methods for mobile automation (Android & iOS).
 * <p>Provides platform checks, ADB helpers, gestures, element checks, and text/image handling.
 */
@Slf4j
public final class MobileUtils {

    private MobileUtils() {
    }

    //region DEVICE ACTIONS

    /**
     * Clears and updates a text field with new value.
     */
    public static void updateValue(WebElement element, String value) {
        element.click();
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Performs a tap action by coordinates via W3C actions.
     */
    public static void tapByCoordinates(int x, int y, AppiumDriver driver) {
        log.info("{} {} Tapping coordinates ({}, {})", INFO_SHORT, INFO, x, y);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    /**
     * Taps a neutral part of the screen (center-top quarter).
     */
    public static void tapNeutralArea(AppiumDriver driver) {
        int w = driver.manage().window().getSize().getWidth();
        int h = driver.manage().window().getSize().getHeight();

        int x = w / 2;
        int y = h / 4;

        tapByCoordinates(x, y, driver);
    }

    /**
     * Performs a swipe up gesture (iOS: swipe, Android: swipeGesture).
     */
    public static void swipeUp(AppiumDriver driver) {
        log.info("{} {} Swipe up gesture", INFO_SHORT, INFO);

        if (isIos()) {
            driver.executeScript("mobile: swipe", java.util.Map.of("direction", "up"));
        } else {
            driver.executeScript("mobile: swipeGesture",
                    java.util.Map.of(
                            "left", 100,
                            "top", 600,
                            "width", 800,
                            "height", 1000,
                            "direction", "up",
                            "percent", 0.85
                    ));
        }
    }

    public static WebDriver unwrap(WebDriver driver) {
        try {
            if (AopUtils.isAopProxy(driver)) {
                return (WebDriver) ((Advised) driver)
                        .getTargetSource()
                        .getTarget();
            }
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to unwrap WebDriver proxy", e);
        }
    }

    public static void controlApp(String action, String appId, AppiumDriver driver) {
        WebDriver realDriver = MobileUtils.unwrap(driver);

        log.info("📱 System action requested: {} for app: {}", action, appId);
        log.info("Driver class: {}", realDriver.getClass().getName());

        if (realDriver instanceof AndroidDriver androidDriver) {
            switch (action.toLowerCase()) {
                case "terminates" -> {
                    log.info("🤖 Android → terminating app: {}", appId);
                    androidDriver.terminateApp(appId);
                }
                case "activates" -> {
                    log.info("🤖 Android → activating app: {}", appId);
                    androidDriver.activateApp(appId);
                }
                default -> throw new IllegalArgumentException(
                        "Unsupported system action: " + action);
            }
        } else if (realDriver instanceof IOSDriver iosDriver) {
            switch (action.toLowerCase()) {
                case "terminates" -> {
                    log.info("🍎 iOS → terminating app: {}", appId);
                    iosDriver.executeScript(
                            "mobile: terminateApp",
                            Map.of("bundleId", appId)
                    );
                }
                case "activates" -> {
                    log.info("🍎 iOS → activating app: {}", appId);
                    iosDriver.executeScript(
                            "mobile: activateApp",
                            Map.of("bundleId", appId)
                    );
                }
                default -> throw new IllegalArgumentException(
                        "Unsupported system action: " + action);
            }
        } else {
            throw new IllegalStateException("Unsupported driver: " + driver.getClass());
        }
        log.info("✅ System action completed: {}", action);
    }
    //endregion

    //region ELEMENT CHECKERS
    public static boolean isButtonEnabled(WebElement element) {
        try {
            if (isIos()) {
                return Boolean.parseBoolean(element.getAttribute("enabled"));
            } else {
                String enabled = element.getAttribute("enabled");
                String clickable = element.getAttribute("clickable");

                boolean isEnabled = "true".equalsIgnoreCase(enabled);
                boolean isClickable = "true".equalsIgnoreCase(clickable);

                return isEnabled && isClickable;
            }
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * Compares two images and returns true if they differ above a threshold.
     *
     * @param a         first image bytes
     * @param b         second image bytes
     * @param threshold difference threshold
     */
    public static boolean areImagesClearlyDifferent(byte[] a, byte[] b, double threshold) {
        try {
            BufferedImage ia = ImageIO.read(new ByteArrayInputStream(a));
            BufferedImage ib = ImageIO.read(new ByteArrayInputStream(b));
            if (ia == null || ib == null) return false;

            int w = 64, h = 64;
            BufferedImage ra = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            BufferedImage rb = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            Graphics2D g1 = ra.createGraphics();
            g1.drawImage(ia, 0, 0, w, h, null);
            g1.dispose();
            Graphics2D g2 = rb.createGraphics();
            g2.drawImage(ib, 0, 0, w, h, null);
            g2.dispose();

            long sumSq = 0L;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int ca = ra.getRGB(x, y);
                    int cb = rb.getRGB(x, y);
                    int ar = (ca >> 16) & 255, ag = (ca >> 8) & 255, ab = ca & 255;
                    int br = (cb >> 16) & 255, bg = (cb >> 8) & 255, bb = cb & 255;
                    int dr = ar - br, dg = ag - bg, db = ab - bb;
                    sumSq += (long) dr * dr + (long) dg * dg + (long) db * db;
                }
            }

            double mse = sumSq / (double) (w * h * 3 * 255 * 255);
            return mse > threshold;
        } catch (Exception e) {
            return false;
        }
    }
    //endregion

    //region GETTERS

    /**
     * Gets user-visible text for an element (uses "value"/"label" on iOS).
     */
    public static String getActualText(WebElement element) {
        if (isIos()) {
            String value = element.getAttribute("value");
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
            String label = element.getAttribute("label");
            if (label != null && !label.isBlank()) {
                return label.trim();
            }
            return "";
        } else {
            return element.getText() != null
                    ? element.getText().trim()
                    : "";
        }
    }

    public static java.util.List<String> getListOfText(List<WebElement> target) {
        return target.stream()
                .map(MobileUtils::getActualText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public static double getStringAsDouble(String raw) {
        return Double.parseDouble(raw.replaceAll("[^0-9.]", ""));
    }
    //endregion

    //region HARD WAITS
    public static void hardSleep(long seconds, String reason) {
        log.warn("{} {} {} {}s - {} {}",
                WARN_SHORT, YELLOW + WARN + RESET, WAIT,
                seconds, reason, RESET);
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            log.error("Thread sleep failed: {}", e.getMessage());
        }
    }

    public static void redirectionSleep(long seconds, Object caller) {
        hardSleep(seconds, "Before/After redirect to " + getPrettyName(caller));
    }

    public static void stabilizationSleep(long seconds, Object caller) {
        hardSleep(seconds, "Before/After action in " + getPrettyName(caller));
    }

    private static String getPrettyName(Object caller) {
        String rawName = caller.getClass().getSimpleName();
        boolean isBean = rawName.contains("$");
        return isBean
                ? rawName.substring(0, rawName.indexOf("$"))
                : rawName;
    }
    //endregion
}
