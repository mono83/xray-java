package com.github.mono83.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;
import com.github.mono83.xray.Ray;

import java.time.Duration;
import java.util.Objects;

/**
 * Abstract ray implementation built over event emitting.
 */
public abstract class Abstract implements Ray {
    /**
     * Emits provided logging event into emitter processing loop.
     *
     * @param event Event to emit
     */
    protected abstract void emit(LoggingEvent event);

    /**
     * Emits metric logging event into emitter processing loop.
     *
     * @param event Event to emit
     */
    protected abstract void emit(MetricEvent event);

    /**
     * @return Collection of logging arguments current ray is configured with
     */
    protected abstract Args getArgs();

    /**
     * Utility method, used to compose metric name
     *
     * @param suffix Metric suffix
     * @return Metric name
     */
    protected abstract String composeMetricName(String suffix);

    @Override
    public void trace(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.TRACE,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void debug(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.DEBUG,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void info(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.INFO,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void warning(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.WARNING,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void error(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.ERROR,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void alert(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.ALERT,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void emergency(final String message, final Arg... args) {
        emit(new LoggingEvent(
                LoggingEvent.Level.EMERGENCY,
                message,
                getArgs(),
                args
        ));
    }

    @Override
    public void inc(final String key, final long value, final Arg... args) {
        emit(new MetricEvent(
                MetricEvent.Type.INCREMENT,
                composeMetricName(key),
                value,
                getArgs(),
                args
        ));
    }

    @Override
    public void gauge(final String key, final long value, final Arg... args) {
        emit(new MetricEvent(
                MetricEvent.Type.GAUGE,
                composeMetricName(key),
                value,
                getArgs(),
                args
        ));
    }

    @Override
    public void duration(final String key, final Duration duration, final Arg... args) {
        emit(new MetricEvent(
                MetricEvent.Type.GAUGE,
                composeMetricName(key),
                Objects.requireNonNull(duration, "duration").toNanos(),
                getArgs(),
                args
        ));
    }
}
