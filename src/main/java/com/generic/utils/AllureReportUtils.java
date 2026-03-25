package com.generic.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;

@Slf4j
public class AllureReportUtils {

    /**
     * Writes Allure environment properties to a file.
     *
     * @param platform  the platform name
     * @param allureDir the Allure results directory
     */
    public static void writeAllureEnvironment(String platform, String allureDir) {
        try {
            File dir = new File(allureDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File env = new File(dir, "environment.properties");

            try (FileWriter writer = new FileWriter(env, false)) {
                writer.write("Platform=" + platform + "\n");
                writer.write("ReportTitle=" + platform + " REPORT\n");
                writer.write("ExecutionDate=" + java.time.LocalDate.now() + "\n");
            }

            log.info("✔ Allure environment.properties written to {}", env.getAbsolutePath());
        } catch (Exception e) {
            log.error("❌ Failed to write environment.properties: {}", e.getMessage());
        }
    }

    /**
     * Writes Allure executor information to a file.
     *
     * @param platform  the platform name
     * @param allureDir the Allure results directory
     */
    public static void writeExecutor(String platform, String allureDir) {
        try {
            File dir = new File(allureDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File exec = new File(dir, "executor.json");

            String json = "{\n" +
                    "  \"name\": \"" + platform + " REPORT\",\n" +
                    "  \"type\": \"local\",\n" +
                    "  \"reportName\": \"" + platform + " REPORT\",\n" +
                    "  \"buildOrder\": 1\n" +
                    "}";

            try (FileWriter writer = new FileWriter(exec, false)) {
                writer.write(json);
            }

            log.info("✔ executor.json written for {}", platform);
        } catch (Exception e) {
            log.error("❌ Failed to write executor.json: {}", e.getMessage());
        }
    }
}
