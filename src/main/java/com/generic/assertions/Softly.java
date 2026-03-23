package com.generic.assertions;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;

public final class Softly {

    private Softly() {}

    private static final ThreadLocal<SoftAssertions> SOFTLY = ThreadLocal.withInitial(SoftAssertions::new);

    private static SoftAssertions getSoftly() {
        return SOFTLY.get();
    }

    public static void isTrue(boolean condition, String context) {
        getSoftly().assertThat(condition)
                .as("%s | Condition: <%s> should be true: ", context, condition)
                .isTrue();
    }

    public static void isFalse(boolean condition, String context) {
        getSoftly().assertThat(condition)
                .as("%s | Condition: <%s> should be false", context, condition)
                .isFalse();
    }

    public static <T> void isEqual(T actual, T expected, String context) {
        getSoftly().assertThat(actual)
                .as("%s | The actual: <%s> is equal to: <%s>", context, actual, expected)
                .isEqualTo(expected);
    }

    public static void isDoubleEqual(double actual, double expected, double offset, String context) {
        getSoftly().assertThat(actual)
                .as("%s | The actual: <%s> is equal to: <%s>", context, actual, expected)
                .isCloseTo(expected, Offset.offset(offset));
    }

    public static void fail(String errorMessage) {
        getSoftly().fail(errorMessage);
    }

    public static void assertAll() {
        try {
            getSoftly().assertAll();
        } finally {
            SOFTLY.remove();
        }
    }
}
