package com.generic.steps;

import com.generic.utils.MobileUtils;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.And;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import static com.generic.mobile.TestEnvironment.isAndroid;

@Slf4j
@RequiredArgsConstructor
public class SystemSteps {

    private final AppiumDriver driver;
    private final Environment env;

    /**
     * Step: system waits for specified seconds.
     *
     * @param seconds the number of seconds to wait
     */
    @And("system waits for {int} seconds")
    public void systemWaitsForSeconds(long seconds) {
        MobileUtils.stabilizationSleep(seconds, this);
    }

    /**
     * Step: user taps neutral area.
     */
    @And("user taps neutral area")
    public void userTapsNeutralArea() {
        MobileUtils.tapNeutralArea(driver);
    }

    /**
     * Step: user swipes up multiple times.
     *
     * @param limit the number of swipes
     */
    @And("user swipes up {int} times")
    public void userSwipesUp(int limit) {
        MobileUtils.stabilizationSleep(1, this);
        for (int i = 0; i < limit; i++) {
            MobileUtils.swipeUp(driver);
            MobileUtils.stabilizationSleep(1, this);
        }
    }

    /**
     * Step: system controls the app (terminates/activates/etc).
     *
     * @param action the action to perform
     */
    @And("system {string} the app")
    public void systemControlsTheApp(String action) {
        String appName = isAndroid()
                ? env.getRequiredProperty("android.app.appPackage")
                : env.getRequiredProperty("ios.rider.bundleId");

        MobileUtils.controlApp(action, appName, driver);
    }
}
