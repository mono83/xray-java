package com.github.mono83.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;

import java.util.Objects;

public class MetricEvent {
    public final long millis;
    public final Type type;
    public final String message;
    public final long value;
    public final Args args;

    public MetricEvent(
            final Type type,
            final String name,
            final long value,
            final Args rayArgs,
            final Arg... args
    ) {
        this.millis = System.currentTimeMillis();
        this.type = Objects.requireNonNull(type, "type");
        this.message = Objects.requireNonNull(name, "name");
        this.value = value;
        this.args = null; // TODO lazy args merging
    }

    public enum Type {
        INCREMENT, GAUGE, DURATION_NANO
    }
}
