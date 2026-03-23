package com.generic.context;

public final class TestFailureContext {

    private static final ThreadLocal<Throwable> LAST_ERROR = new ThreadLocal<>();

    private TestFailureContext() {}

    /**
     * Sets the last error that occurred during testing.
     *
     * @param throwable the throwable error
     */
    public static void setError(Throwable throwable) {
        LAST_ERROR.set(throwable);
    }

    /**
     * @return the last error that occurred
     */
    public static Throwable getError() {
        return LAST_ERROR.get();
    }

    /**
     * Clears the last error.
     */
    public static void clear() {
        LAST_ERROR.remove();
    }
}
