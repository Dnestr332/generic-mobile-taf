package com.generic.assertions;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;

public final class Softly {

    private Softly() {}

    private static final ThreadLocal<SoftAssertions> SOFTLY = ThreadLocal.withInitial(SoftAssertions::new);

    private static SoftAssertions getSoftly() {
        return SOFTLY.get();
    }

    /**
     * Asserts that a condition is true (softly).
     *
     * @param condition the condition to check
     * @param context   the context message for the assertion
     */
    public static void isTrue(boolean condition, String context) {
        getSoftly().assertThat(condition)
                .as("%s | Condition: <%s> should be true: ", context, condition)
                .isTrue();
    }

    /**
     * Asserts that a condition is false (softly).
     *
     * @param condition the condition to check
     * @param context   the context message for the assertion
     */
    public static void isFalse(boolean condition, String context) {
        getSoftly().assertThat(condition)
                .as("%s | Condition: <%s> should be false", context, condition)
                .isFalse();
    }

    /**
     * Asserts that two objects are equal (softly).
     *
     * @param actual   the actual value
     * @param expected the expected value
     * @param context  the context message for the assertion
     * @param <T>      the type of the objects being compared
     */
    public static <T> void isEqual(T actual, T expected, String context) {
        getSoftly().assertThat(actual)
                .as("%s | The actual: <%s> is equal to: <%s>", context, actual, expected)
                .isEqualTo(expected);
    }

    /**
     * Asserts that two doubles are equal with a specified offset (softly).
     *
     * @param actual   the actual double value
     * @param expected the expected double value
     * @param offset   the allowed offset for comparison
     * @param context  the context message for the assertion
     */
    public static void isDoubleEqual(double actual, double expected, double offset, String context) {
        getSoftly().assertThat(actual)
                .as("%s | The actual: <%s> is equal to: <%s>", context, actual, expected)
                .isCloseTo(expected, Offset.offset(offset));
    }

    /**
     * Fails the assertion with a given error message (softly).
     *
     * @param errorMessage the error message to display
     */
    public static void fail(String errorMessage) {
        getSoftly().fail(errorMessage);
    }

    /**
     * Asserts all soft assertions and resets the ThreadLocal instance.
     */
    public static void assertAll() {
        try {
            getSoftly().assertAll();
        } finally {
            SOFTLY.remove();
        }
    }
}
