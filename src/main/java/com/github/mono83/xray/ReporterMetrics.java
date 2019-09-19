package com.github.mono83.xray;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Describes components, able to process metrics data.
 */
public interface ReporterMetrics {
    /**
     * Reports increment with value 1.
     *
     * @param key  Metric key
     * @param args Arguments, optional
     */
    default void inc(String key, Arg... args) {
        this.inc(key, 1, args);
    }

    /**
     * Reports increment.
     *
     * @param key   Metric key
     * @param value Increment value
     * @param args  Arguments, optional
     */
    void inc(String key, long value, Arg... args);

    /**
     * Reports gauge.
     *
     * @param key   Metric key
     * @param value Gauge value
     * @param args  Arguments, optional
     */
    void gauge(String key, long value, Arg... args);

    /**
     * Reports duration.
     *
     * @param key      Metric key
     * @param duration Duration value
     * @param args     Arguments, optional
     */
    void duration(String key, Duration duration, Arg... args);

    /**
     * Executes provided {@link Runnable} function and sends execution time
     * metrics with nanoseconds precision.
     *
     * @param func Function to execute
     * @param key  Metric key
     * @param args Arguments, optional
     */
    default void duration(final Runnable func, final String key, Arg... args) {
        Objects.requireNonNull(func, "func");
        Objects.requireNonNull(key, "key");
        long before = System.nanoTime();
        func.run();
        this.duration(key, Duration.ofNanos(System.nanoTime() - before), args);
    }

    /**
     * Executes provided {@link Supplier} function and sends execution time
     * metrics with nanoseconds precision.
     *
     * @param func Supplier to execute
     * @param key  Metric key
     * @param args Arguments, optional
     * @param <T> Response type
     * @return Value, obtained from supplier
     */
    default <T> T duration(final Supplier<T> func, final String key, Arg... args) {
        Objects.requireNonNull(func, "func");
        Objects.requireNonNull(key, "key");
        long before = System.nanoTime();
        T result = func.get();
        this.duration(key, Duration.ofNanos(System.nanoTime() - before), args);
        return result;
    }
}
