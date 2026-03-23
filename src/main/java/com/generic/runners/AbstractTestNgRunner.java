package com.generic.runners;

import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.*;

public abstract class AbstractTestNgRunner {

    protected TestNGCucumberRunner testRunner;

    /**
     * @return the default platform for this runner
     */
    protected abstract String defaultPlatform();

    /**
     * Initialization hook called before TestNG Cucumber runner starts.
     *
     * @param platform the platform name
     */
    protected abstract void beforeRunnerInit(String platform);

    /**
     * Sets up the TestNG Cucumber runner.
     *
     * @param platform the platform parameter from TestNG XML
     */
    @BeforeClass(alwaysRun = true)
    @Parameters({"platform"})
    public void setupClass(@Optional String platform) {
        String resolvedPlatform = platform != null ? platform : defaultPlatform();
        System.setProperty("platform", resolvedPlatform);
        beforeRunnerInit(resolvedPlatform);

        testRunner = new TestNGCucumberRunner(this.getClass());
    }

    /**
     * @return scenarios for the DataProvider
     */
    @DataProvider
    public Object[][] scenarios() {
        return testRunner.provideScenarios();
    }

    /**
     * Runs a single Cucumber scenario.
     *
     * @param pickle  the pickle wrapper
     * @param feature the feature wrapper
     */
    @Test(dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper feature) {
        testRunner.runScenario(pickle.getPickle());
    }

    /**
     * Tears down the TestNG Cucumber runner.
     */
    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testRunner.finish();
    }
}
