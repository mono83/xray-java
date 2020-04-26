package com.github.mono83.xray;

import java.util.Objects;

/**
 * Argument implementation for empty value.
 */
public final class ArgNull implements Arg {
    /**
     * Contains logging argument name.
     */
    private final String name;

    /**
     * Constructs new logging argument without value.
     *
     * @param name Argument name
     */
    ArgNull(final String name) {
        Objects.requireNonNull(name, "name");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
        return name.equals(argNull.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
