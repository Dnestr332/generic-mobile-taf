package com.generic.steps.hooks;

import com.generic.assertions.Softly;
import com.generic.context.TestFailureContext;
import com.generic.enums.Platform;
import com.generic.mobile.MobileDriverFactory;
import com.generic.mobile.TestEnvironment;
import com.generic.service.AllureService;
import com.generic.utils.DataBaseUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertionError;

import static com.generic.logs.LogStyles.*;

@Slf4j
@RequiredArgsConstructor
public class MobileHooks {

    private final MobileDriverFactory mobileDriverFactory;
    private final AllureService allureService;

    /**
     * Hook to prepare the Appium server before tests (local only).
     */
    @Before(order = -2)
    public void prepareAppium() {
        if (TestEnvironment.isCi()) {
            return;
        }
        mobileDriverFactory.startLocalServer();
    }

    /**
     * Hook to log the test platform before each scenario.
     *
     * @param scenario the Cucumber scenario
     */
    @Before(order = 1)
    public void annotatePlatform(Scenario scenario) {
        Platform platform = TestEnvironment.getPlatform();

        log.info(BLUE + "================= TEST PLATFORM =================" + RESET);
        log.info("{} {} Running on {}{}{}", INFO_SHORT, LENNY, GREEN, platform, RESET);
        log.info(BLUE + "=================================================" + RESET);
    }

    /**
     * Hook to establish DB connection for scenarios tagged with @db.
     */
    @Before("@db")
    public void getConnection() {
        try {
            DataBaseUtils.createConnection();
            log.info("{} {} DB connection established{}", OK_SHORT, BEAR, RESET);
        } catch (Exception e) {
            log.error("{} {} DB connection FAILED → {}",
                    FAIL_SHORT, TABLEFLIP, e.getMessage());
        }
    }

    //region AFTER HOOKS
    /**
     * Hook to attach a screenshot if the scenario fails.
     *
     * @param scenario the Cucumber scenario
     */
    @After(order = 2)
    public void screenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            allureService.attachScreenshot();
        }
    }

    /**
     * Hook to close the DB connection for scenarios tagged with @db.
     */
    @After("@db")
    public void killConnection() {
        log.info("{} {} Closing DB connection...", INFO_SHORT, UNFLIP);
        DataBaseUtils.destroy();
    }

    /**
     * Hook to assert all soft assertions at the end of each scenario.
     */
    @After
    public void tearDownSoftAssert() {
        try {
            Softly.assertAll();
        } catch (SoftAssertionError e) {
            TestFailureContext.setError(e);
            throw e;
        }
    }
}
