package com.github.mono83.xray;

import java.util.Arrays;
import java.util.Objects;

/**
 * Generic argument implementation, that supports all kinds of objects.
 *
 * @param <T> Type of values within argument
 */
public class ArgGeneric<T> implements Arg {
    /**
     * Contains logging argument name.
     */
    private final String name;
    /**
     * Contains argument values.
     */
    private final T[] values;

    /**
     * Constructs logging argument with generic values.
     *
     * @param name   Logging argument name
     * @param values Argument values
     */
    ArgGeneric(final String name, final T[] values) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");

        this.name = name;
        this.values = values;
    }

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param name   Argument name
     * @param values Argument values
     * @param <T>    Generic argument type
     * @return Logging argument
     */
    public static <T> Arg of(final String name, final T[] values) {
        if (values == null || values.length == 0) {
            return new ArgNull(name);
        }

        return new ArgGeneric<>(name, values);
    }

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param name   Argument name
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String name, final String... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(name);
        }

        return new ArgGeneric<>(name, values);
    }

    /**
     * Builds and returns generic argument instance for provided values.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param name   Argument name
     * @param values Argument values
     * @return Logging argument
     */
    public static Arg of(final String name, final Enum... values) {
        if (values == null || values.length == 0) {
            return new ArgNull(name);
        }

        return new ArgGeneric<>(name, values);
    }

    @Override
    public String getName() {
        return name;
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
        return name.equals(arg.name) && Arrays.equals(values, arg.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }
}
