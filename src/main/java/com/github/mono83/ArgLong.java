package com.github.mono83;

import java.util.Arrays;
import java.util.Objects;

/**
 * Argument implementation for longs.
 */
public class ArgLong implements Arg {
    /**
     * Contains logging argument key (name).
     */
    private final String key;
    /**
     * Contains argument values.
     */
    private final long[] values;

    /**
     * Builds and returns long argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key    Argument key (name)
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String key, final long... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(key);
        }

        return new ArgLong(key, values);
    }

    /**
     * Builds and returns long argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key    Argument key (name)
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String key, final int... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(key);
        }

        return new ArgLong(key, Arrays.stream(values).asLongStream().toArray());
    }

    /**
     * Constructs logging argument with long values.
     *
     * @param key    Logging argument key (name)
     * @param values Argument values
     */
    ArgLong(final String key, final long... values) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(values, "values");
        this.key = key;
        this.values = values;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue(final int index) {
        if (values.length == 0) {
            return "";
        }
        if (index < 0 || index >= values.length) {
            return Long.toString(values[values.length - 1]);
        }
        return Long.toString(values[index]);
    }

    /**
     * Getter, that returns native value of provided argument.
     *
     * @param index Index
     * @return Found value
     * @throws IndexOutOfBoundsException If index out of range
     *                                   or values array is empty
     */
    public long get(final int index) throws IndexOutOfBoundsException {
        if (values.length == 0 || index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException(index);
        }

        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArgLong argLong = (ArgLong) o;
        return key.equals(argLong.key) && Arrays.equals(values, argLong.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(key);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
