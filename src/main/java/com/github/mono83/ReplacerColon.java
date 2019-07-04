package com.github.mono83;

import java.util.HashMap;
import java.util.function.BiFunction;

/**
 * Replaces placeholders in colon-prefixed format (like :name).
 */
public class ReplacerColon implements BiFunction<String, Args, String> {
    @Override
    public String apply(final String s, final Args args) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        if (args == null || args.isEmpty()) {
            return s;
        }

        // Replacing
        Builder result = new Builder(args);
        StringBuilder key = null;
        boolean space = true;
        for (char c : s.toCharArray()) {
            if (c == ':') {
                if (space) {
                    key = new StringBuilder();
                    continue;
                }
            }
            space = false;
            if (c == ' ') {
                space = true;
                if (key != null) {
                    result.append(key);
                    key = null;
                }
            } else if (key != null && (c == '.' || c == ',' || c == '-')) {
                // Punctuation marks also separates placeholders
                result.append(key);
                key = null;
            }

            if (key != null) {
                key.append(c);
            } else {
                result.append(c);
            }
        }
        if (key != null) {
            result.append(key);
        }

        return result.sb.toString();
    }

    /**
     * Utility wrapper over string builder.
     */
    private static class Builder {
        /**
         * String builder.
         */
        private final StringBuilder sb = new StringBuilder();
        /**
         * Used to calculate amount of occurrences of argument with same name.
         */
        private final HashMap<String, Integer> evidences = new HashMap<>();
        /**
         * Provided logging arguments collection.
         */
        private final Args args;

        /**
         * Constructs builder.
         *
         * @param args Provided logging arguments
         */
        Builder(final Args args) {
            this.args = args;
        }

        /**
         * Appends character to string builder.
         *
         * @param c Char to append
         */
        void append(final char c) {
            sb.append(c);
        }

        /**
         * Appends value from arguments, resolved by name stored within provided StringBuilder.
         *
         * @param key String builder with key
         */
        void append(final StringBuilder key) {
            // Resolving argument
            final String name = key.toString();
            args.get(name).ifPresentOrElse(
                    arg -> {
                        int index = evidences.computeIfAbsent(arg.getKey(), s -> 0);
                        sb.append(arg.getValue(index++));
                        evidences.put(arg.getKey(), index);
                    },
                    () -> sb.append("<!").append(name).append("!>")
            );
        }
    }
}
