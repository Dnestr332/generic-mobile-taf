package com.generic.runners;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractFailedRunner extends AbstractTestNgRunner {

    /**
     * @param platform the platform name
     * @return the path to the rerun file
     */
    protected abstract String resolveRerunFile(String platform);

    /**
     * @param platform the platform name
     * @return the path to the Allure results directory
     */
    protected abstract String resolveAllureDir(String platform);

    /**
     * Performs initialization before the runner starts.
     *
     * @param platform the platform name
     */
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
