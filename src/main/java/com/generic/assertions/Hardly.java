package com.generic.assertions;

import org.assertj.core.api.Assertions;

public final class Hardly {

    private Hardly() {}

    /**
     * Asserts that a condition is true.
     *
     * @param condition the condition to check
     * @param context   the context message for the assertion
     */
    public static void isTrue(boolean condition, String context) {
        Assertions.assertThat(condition)
                .as("%s | Condition: <%s> should be true: ", context, condition)
                .isTrue();
    }

    /**
     * Asserts that a condition is false.
     *
     * @param condition the condition to check
     * @param context   the context message for the assertion
     */
    public static void isFalse(boolean condition, String context) {
        Assertions.assertThat(condition)
                .as("%s | Condition: <%s> should be false", context, condition)
                .isFalse();
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param actual   the actual value
     * @param expected the expected value
     * @param context  the context message for the assertion
     * @param <T>      the type of the objects being compared
     */
    public static <T> void isEqual(T actual, T expected, String context) {
        Assertions.assertThat(actual)
                .as("%s | The actual: <%s> is equal to: <%s>", context, actual, expected)
                .isEqualTo(expected);
    }

    /**
     * Fails the assertion with a given error message.
     *
     * @param errorMessage the error message to display
     */
    public static void fail(String errorMessage) {
        Assertions.fail(errorMessage);
    }
}
