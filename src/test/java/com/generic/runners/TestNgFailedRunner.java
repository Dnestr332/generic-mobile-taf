package com.generic.runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        glue = {"com.generic.steps", "com.generic.steps.hooks"},
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestNgFailedRunner extends AbstractFailedRunner{

    @Override
    protected String defaultPlatform() {
        return "ANDROID";
    }

    @Override
    protected String resolveRerunFile(String platform) {
        return platform.equals("ANDROID")
                ? "target/rerun/android.txt"
                : "target/rerun/ios.txt";
    }

    @Override
    protected String resolveAllureDir(String platform) {
        return platform.equals("ANDROID")
                ? "target/allure-results/rerun-android"
                : "target/allure-results/rerun-ios";
    }
}
