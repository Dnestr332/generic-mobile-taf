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

    /**
     * Prints a POJO as pretty-printed JSON.
     *
     * @param pojo the object to print
     */
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

    /**
     * Waits until a condition is met.
     *
     * @param condition the boolean condition supplier
     * @param seconds   maximum wait time in seconds
     */
    public static void waitUntil(BooleanSupplier condition, int seconds) {
        Awaitility.await()
                .atMost(Duration.ofSeconds(seconds))
                .until(condition::getAsBoolean);
    }

    /**
     * Waits until a condition is met and returns the result.
     *
     * @param condition the boolean condition supplier
     * @param seconds   maximum wait time in seconds
     * @return true if condition was met, false otherwise
     */
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
