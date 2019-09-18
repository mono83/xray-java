package com.github.mono83.xray.events.consumers.filters;

import com.github.mono83.xray.events.LoggingEvent;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Filters all logging events depending on logging level of events.
 */
public class LogLevelAtLeast implements Consumer<LoggingEvent> {
    /**
     * Minimal logging level to pass, inclusive.
     */
    private final LoggingEvent.Level threshold;
    /**
     * Next consumer, that will be invoked after predicate success testing.
     */
    private final Consumer<LoggingEvent> next;

    /**
     * Constructs new log level filter.
     *
     * @param threshold Minimal logging level to pass, inclusive
     * @param next      Next consumer, that will be invoked after predicate success testing
     */
    public LogLevelAtLeast(
            final LoggingEvent.Level threshold,
            final Consumer<LoggingEvent> next
    ) {
        this.threshold = Objects.requireNonNull(threshold, "threshold");
        this.next = Objects.requireNonNull(next, "next");
    }

    @Override
    public void accept(final LoggingEvent event) {
        if (event != null && event.level.ordinal() >= threshold.ordinal()) {
            next.accept(event);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "[Log level at least %s and then %s]",
                threshold.toString(),
                next.toString()
        );
    }
}
