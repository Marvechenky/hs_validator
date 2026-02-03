package com.polaris.HS.Code.Validator.util;

public class HsCodeNormalizer {

    public static String normalize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("HS code cannot be null");
        }

        return input
                .trim()
                .replace(".", "")
                .replace(" ", "");
    }
}
