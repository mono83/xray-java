package com.github.mono83;

import java.util.Arrays;
import java.util.Objects;

/**
 * Generic argument implementation, that supports all kinds of objects.
 *
 * @param <T> Type of values within argument
 */
public class ArgGeneric<T> implements Arg {
    /**
     * Contains logging argument key (name).
     */
    private final String key;
    /**
     * Contains argument values.
     */
    private final T[] values;

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key    Argument key (name)
     * @param values Argument values
     * @param <T>    Generic argument type
     * @return Logging argument
     */
    public static <T> Arg of(final String key, final T[] values) {
        if (values == null || values.length == 0) {
            return new ArgNull(key);
        }

        return new ArgGeneric<>(key, values);
    }

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key    Argument key (name)
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String key, final String... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(key);
        }

        return new ArgGeneric<>(key, values);
    }

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key    Argument key (name)
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String key, final Enum... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(key);
        }

        return new ArgGeneric<>(key, values);
    }

    /**
     * Constructs logging argument with generic values.
     *
     * @param key    Logging argument key (name)
     * @param values Argument values
     */
    ArgGeneric(final String key, final T[] values) {
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
            return toString(values[values.length - 1]);
        }
        return toString(values[index]);
    }

    /**
     * Getter, that returns native value of provided argument.
     *
     * @param index Index
     * @return Found value
     * @throws IndexOutOfBoundsException If index out of range
     *                                   or values array is empty
     */
    public T get(final int index) throws IndexOutOfBoundsException {
        if (values.length == 0 || index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException(index);
        }

        return values[index];
    }

    /**
     * Converts provided value into string representation.
     * To be used in cases, when custom conversion to string should be applied.
     *
     * @param value Value to convert
     * @return String representation of value
     */
    protected String toString(final T value) {
        return value.toString();
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
        ArgGeneric arg = (ArgGeneric) o;
        return key.equals(arg.key) && Arrays.equals(values, arg.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(key);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
