package com.generic.mobile;

import com.generic.enums.Platform;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.spring.ScenarioScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static com.generic.logs.LogStyles.*;
import static com.generic.mobile.TestEnvironment.isAndroid;

@Slf4j
@Component
@ScenarioScope
@RequiredArgsConstructor
public class MobileDriverFactory {

    private AppiumDriverLocalService service;
    private final Environment env;

    public AppiumDriver createDriver() {
        Platform platform = TestEnvironment.getPlatform();

        log.info("{} {} Creating AppiumDriver for platform: {}{}",
                INFO_SHORT, INFO, GREEN + platform, RESET
        );

        if (platform.isAndroid()) disablePushNotifications();

        return switch (platform) {
            case ANDROID -> new AndroidDriver(getAndroidUrl(), androidAppOptions());
            case IOS -> new IOSDriver(getIosUrl(), iosAppOptions());
        };
    }

    // region SERVER MANAGEMENT
    private URL getAndroidUrl() {
        try {
            return URI.create(env.getRequiredProperty("appium.androidUrl")).toURL();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Android Appium server URL", e);
        }
    }

    private URL getIosUrl() {
        try {
            return URI.create(env.getRequiredProperty("appium.iosUrl")).toURL();
        } catch (Exception e) {
            throw new RuntimeException("Invalid iOS Appium server URL", e);
        }
    }
    // endregion

    //region START SERVER
    public void startLocalServer() {
        int port = isAndroid() ? 4723 : 4725;

        if (isServerRunning(port)) {
            log.info("{} Appium server already running on {}", INFO_SHORT, port);
            return;
        }

        log.info("{} Starting Appium server on {}…", INFO_SHORT, port);
        service = AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder()
                        .withIPAddress("127.0.0.1")
                        .usingPort(port)
                        .withArgument(() -> "--log-level", "error")
                        .withLogOutput(new OutputStream() {
                            @Override
                            public void write(int b) {
                            }
                        })
        );

        service.start();
        log.info("{} Appium server started on {}", OK_SHORT, port);
    }

    public void stopLocalServer() {
        if (service != null && service.isRunning()) {
            log.info("{} Stopping Appium server…", INFO_SHORT);
            service.stop();
            log.info("{} Appium server stopped{}", OK_SHORT, RESET);
        }
    }

    public boolean isServerRunning(int port) {
        try (Socket socket = new Socket("127.0.0.1", port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //endregion

    //region ANDROID BUILDERS
    private UiAutomator2Options baseAndroidOptions() {
        String deviceName = env.getRequiredProperty("android.deviceName");
        String platformVersion = env.getRequiredProperty("android.platformVersion");

        return new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UIAutomator2")
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(300))
                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(120))
                .setUiautomator2ServerInstallTimeout(Duration.ofSeconds(120))
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .amend("avdLaunchTimeout", 180000)
                .amend("avdReadyTimeout", 180000)
                .amend("waitForIdleTimeout", 10)
                .amend("uiautomator2ServerInstallTimeout", 180000)
                .amend("uiautomator2ServerLaunchTimeout", 180000)
                .amend("enforceXPath1", true);
    }

    private UiAutomator2Options androidAppOptions() {
        return baseAndroidOptions()
                .setAppPackage(env.getRequiredProperty("android.app.appPackage"))
                .setAppActivity(env.getRequiredProperty("android.app.appActivity"));
    }
    //endregion

    //region IOS BUILDERS
    private XCUITestOptions baseIosOptions() {
        String deviceName = env.getRequiredProperty("ios.deviceName");
        String platformVersion = env.getRequiredProperty("ios.platformVersion");

        return new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName("XCUITest")
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setNoReset(false)
                .setNewCommandTimeout(Duration.ofSeconds(300))
                .setWdaLaunchTimeout(Duration.ofSeconds(120))
                .setSimpleIsVisibleCheck(true)
                .amend("enforceXPath1", true);
    }

    private XCUITestOptions iosAppOptions() {
        String bundleId = env.getRequiredProperty("ios.app.bundleId");

        return baseIosOptions()
                .setBundleId(bundleId);
    }
    //endregion

    //region HELPERS
    private void disablePushNotifications() {
        try {
            Runtime.getRuntime().exec(new String[]{
                    "adb", "shell", "settings", "put", "global", "heads_up_notifications_enabled", "0"
            });
        } catch (Exception e) {
            log.warn("⚠ Failed to disable Android push notifications: {}", e.getMessage());
        }
    }
    //endregion
}
