package com.github.mono83.xray.events.consumers.filters;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Filter for logging and metric events processing based on {@see java.util.function.Predicate}.
 *
 * @param <T> {@link com.github.mono83.xray.events.LoggingEvent} or {@link com.github.mono83.xray.events.MetricEvent}
 */
public class Predicate<T> implements Consumer<T> {
    /**
     * Predicate condition.
     */
    private final java.util.function.Predicate<T> predicate;
    /**
     * Next consumer, that will be invoked after predicate success testing.
     */
    private final Consumer<T> next;

    /**
     * Constructs new filter.
     *
     * @param predicate Predicate condition
     * @param next      Next consumer, that will be invoked after predicate success testing
     */
    public Predicate(
            final java.util.function.Predicate<T> predicate,
            final Consumer<T> next
    ) {
        this.predicate = Objects.requireNonNull(predicate, "predicate");
        this.next = Objects.requireNonNull(next, "next");
    }

    @Override
    public void accept(final T t) {
        if (t != null && predicate.test(t)) {
            next.accept(t);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "[Match %s and then %s]",
                predicate.toString(),
                next.toString()
        );
    }
}
