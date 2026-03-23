package com.generic.runners;

import com.generic.utils.AllureReportUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractMainRunner extends AbstractTestNgRunner {

    protected abstract String allureSubFolder();
    protected abstract String rerunFileName();

    @Override
    protected void beforeRunnerInit(String platform) {
        resetRerunFile();
        setupAllure(platform);
    }

    private String baseDir() {
        return System.getProperty("user.dir") + "/target";
    }

    private void resetRerunFile() {
        Path rerunFile = Paths.get(baseDir() + "/rerun/" + rerunFileName());
        try {
            Files.deleteIfExists(rerunFile);
            Files.createDirectories(rerunFile.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupAllure(String platform) {
        String allureDir = baseDir() + "/allure-results/" + allureSubFolder();
        System.setProperty("allure.results.directory", allureDir);

        AllureReportUtils.writeAllureEnvironment(platform, allureDir);
        AllureReportUtils.writeExecutor(platform, allureDir);
    }
}
