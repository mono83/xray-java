package com.github.mono83.xray.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;

import java.util.Objects;

/**
 * Contains metric data.
 */
public class MetricEvent {
    /**
     * Event emission time in milliseconds.
     */
    public final long millis;
    /**
     * Metric type.
     */
    public final Type type;
    /**
     * Metric name.
     */
    public final String name;
    /**
     * Metric value.
     * <p>
     * 1. For increments - it holds increment value
     * 2. For gauges - it holds gauge value
     * 3. For durations - it holds elapsed time in nanoseconds
     */
    public final long value;
    /**
     * Metric arguments.
     */
    public final Args args;

    /**
     * Constructs metric event.
     *
     * @param type    Metric type
     * @param name    Metric name
     * @param value   Metric value
     * @param rayArgs Arguments collection, obtained from ray
     * @param args    Arguments collection, provided during event creation
     */
    public MetricEvent(
            final Type type,
            final String name,
            final long value,
            final Args rayArgs,
            final Arg... args
    ) {
        this.millis = System.currentTimeMillis();
        this.type = Objects.requireNonNull(type, "type");
        this.name = Objects.requireNonNull(name, "name");
        this.value = value;
        this.args = LazyMergedArgs.of(rayArgs, args);
    }

    /**
     * Metric type.
     */
    public enum Type {
        INCREMENT, GAUGE, DURATION_NANO
    }
}
