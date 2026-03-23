package com.generic.runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.generic.steps", "com.generic.steps.hooks"},

        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "rerun:target/rerun/ios.txt"
        },
        monochrome = false,
        dryRun = false,

        tags = "@wip and not @ignore"
)
public class TestNgIosRunner extends AbstractMainRunner {

    @Override
    protected String defaultPlatform() {
        return "IOS";
    }

    @Override
    protected String allureSubFolder() {
        return "ios";
    }

    @Override
    protected String rerunFileName() {
        return "ios.txt";
    }
}
