package com.github.mono83.xray;

import java.time.Duration;

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
     * Reports increment
     *
     * @param key   Metric key
     * @param value Increment value
     * @param args  Arguments, optional
     */
    void inc(String key, long value, Arg... args);

    /**
     * Reports gauge
     *
     * @param key   Metric key
     * @param value Gauge value
     * @param args  Arguments, optional
     */
    void gauge(String key, long value, Arg... args);

    /**
     * Reports duration
     *
     * @param key      Metric key
     * @param duration Duration value
     * @param args     Arguments, optional
     */
    void duration(String key, Duration duration, Arg... args);
}
