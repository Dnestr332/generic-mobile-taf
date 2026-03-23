package com.generic.runners;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractFailedRunner extends AbstractTestNgRunner {

    protected abstract String resolveRerunFile(String platform);
    protected abstract String resolveAllureDir(String platform);

    @Override
    protected void beforeRunnerInit(String platform) {
        String rerunFile = resolveRerunFile(platform);
        String allureDir = resolveAllureDir(platform);

        System.setProperty("cucumber.features", "@" + rerunFile);
        System.setProperty("allure.results.directory", allureDir);

        log.info("🔥 RERUN PLATFORM: {}", platform);
        log.info("📄 Rerun file: {}", rerunFile);
        log.info("📁 Allure rerun dir: {}", allureDir);
    }
}
