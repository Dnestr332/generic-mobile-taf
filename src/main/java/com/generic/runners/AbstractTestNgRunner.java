package com.generic.runners;

import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.*;

public abstract class AbstractTestNgRunner {

    protected TestNGCucumberRunner testRunner;

    protected abstract String defaultPlatform();
    protected abstract void beforeRunnerInit(String platform);

    @BeforeClass(alwaysRun = true)
    @Parameters({"platform"})
    public void setupClass(@Optional String platform) {
        String resolvedPlatform = platform != null ? platform : defaultPlatform();
        System.setProperty("platform", resolvedPlatform);
        beforeRunnerInit(resolvedPlatform);

        testRunner = new TestNGCucumberRunner(this.getClass());
    }

    @DataProvider
    public Object[][] scenarios() {
        return testRunner.provideScenarios();
    }

    @Test(dataProvider = "scenarios")
    public void scenario(PickleWrapper pickle, FeatureWrapper feature) {
        testRunner.runScenario(pickle.getPickle());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testRunner.finish();
    }
}
