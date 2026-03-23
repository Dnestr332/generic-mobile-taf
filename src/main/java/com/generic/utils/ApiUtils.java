package com.generic.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;
import java.util.function.BooleanSupplier;

@Slf4j
public final class ApiUtils {

    private ApiUtils() {}

    public static void prettyPrint(Object pojo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String prettyJson = mapper.writeValueAsString(pojo);
            System.out.println(prettyJson);
        } catch (Exception e) {
            log.error("Failed to fetch or print account response: {}", e.getMessage());
        }
    }

    public static void waitUntil(BooleanSupplier condition, int seconds) {
        Awaitility.await()
                .atMost(Duration.ofSeconds(seconds))
                .until(condition::getAsBoolean);
    }

    public static boolean waitUntilAndReturn(BooleanSupplier condition, int seconds) {
        try {
            Awaitility.await()
                    .atMost(Duration.ofSeconds(seconds))
                    .until(condition::getAsBoolean);
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }
}
