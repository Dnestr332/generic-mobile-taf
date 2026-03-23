package com.generic.runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.generic.steps", "com.generic.steps.hooks"},

        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "rerun:target/rerun/android.txt"
        },
        monochrome = false,
        dryRun = false,

        tags = "@wip and not @ignore"
)
public class TestNgAndroidRunner extends AbstractMainRunner {

    @Override
    protected String defaultPlatform() {
        return "ANDROID";
    }

    @Override
    protected String allureSubFolder() {
        return "android";
    }

    @Override
    protected String rerunFileName() {
        return "android.txt";
    }
}
