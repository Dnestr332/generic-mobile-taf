package com.generic.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class EnumUtils {

    private EnumUtils() {}

    /**
     * Parses a string to an enum constant.
     *
     * @param enumClass the enum class
     * @param rawValue  the raw string value
     * @param <T>       the enum type
     * @return the enum constant
     */
    public static <T extends Enum<T>> T parse(Class<T> enumClass, String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Value for %s cannot be null or blank.",
                            enumClass.getSimpleName())
            );
        }

        String key = toEnumKey(rawValue);

        try {
            return Enum.valueOf(enumClass, key);
        } catch (IllegalArgumentException e) {
            String available = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            String errorMessage = String.format(
                    "Invalid value '%s' for %s. Available options: [%s]",
                    rawValue,
                    enumClass.getSimpleName(),
                    available
            );
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Converts a string to a standard enum key (uppercase, underscores).
     *
     * @param s the string to convert
     * @return the formatted key
     */
    public static String toEnumKey(String s) {
        return s == null ? "" :
                s.trim().toUpperCase()
                        .replaceAll("[^A-Z0-9]+", "_")
                        .replaceAll("_+", "_")
                        .replaceAll("^_|_$", "");
    }
}
