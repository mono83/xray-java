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
     * @param name Metric name
     * @param args Arguments, optional
     */
    default void inc(String name, Arg... args) {
        this.inc(name, 1, args);
    }

    /**
     * Reports increment.
     *
     * @param name  Metric name
     * @param value Increment value
     * @param args  Arguments, optional
     */
    void inc(String name, long value, Arg... args);

    /**
     * Reports gauge.
     *
     * @param name  Metric name
     * @param value Gauge value
     * @param args  Arguments, optional
     */
    void gauge(String name, long value, Arg... args);

    /**
     * Reports duration.
     *
     * @param name     Metric name
     * @param duration Duration value
     * @param args     Arguments, optional
     */
    void duration(String name, Duration duration, Arg... args);

    /**
     * Executes provided {@link Runnable} function and sends execution time
     * metrics with nanoseconds precision.
     *
     * @param func Function to execute
     * @param name Metric name
     * @param args Arguments, optional
     */
    default void duration(final Runnable func, final String name, Arg... args) {
        Objects.requireNonNull(func, "func");
        Objects.requireNonNull(name, "name");
        long before = System.nanoTime();
        func.run();
        this.duration(name, Duration.ofNanos(System.nanoTime() - before), args);
    }

    /**
     * Executes provided {@link Supplier} function and sends execution time
     * metrics with nanoseconds precision.
     *
     * @param func Supplier to execute
     * @param name Metric name
     * @param args Arguments, optional
     * @param <T>  Response type
     * @return Value, obtained from supplier
     */
    default <T> T duration(final Supplier<T> func, final String name, Arg... args) {
        Objects.requireNonNull(func, "func");
        Objects.requireNonNull(name, "name");
        long before = System.nanoTime();
        T result = func.get();
        this.duration(name, Duration.ofNanos(System.nanoTime() - before), args);
        return result;
    }
}
