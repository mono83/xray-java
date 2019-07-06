package com.github.mono83.xray;

import java.util.Objects;

/**
 * Argument implementation for empty value.
 */
public final class ArgNull implements Arg {
    /**
     * Contains logging argument key (name).
     */
    private final String key;

    /**
     * Constructs new logging argument without value.
     *
     * @param key Argument key (name)
     */
    ArgNull(final String key) {
        Objects.requireNonNull(key, "key");
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue(final int index) {
        return "";
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArgNull argNull = (ArgNull) o;
        return key.equals(argNull.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
