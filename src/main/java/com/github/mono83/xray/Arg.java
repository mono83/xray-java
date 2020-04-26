package com.github.mono83.xray;

/**
 * Arg represents logging/metrics argument.
 * It contains name and multiple values.
 * Immutable, thread safe.
 */
public interface Arg {
    /**
     * @return Argument name
     */
    String getName();

    /**
     * Returns argument value by index.
     * By contract, if provided index is out of range,
     * argument must return last one
     *
     * @param index Value index
     * @return Argument value
     */
    String getValue(int index);

    /**
     * @return Amount of values inside argument
     */
    int size();

    /**
     * @return True if logging argument contains no values
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
