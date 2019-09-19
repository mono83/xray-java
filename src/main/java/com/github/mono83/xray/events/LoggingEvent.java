package com.github.mono83.xray.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;

import java.util.Objects;

/**
 * Contains logging data.
 */
public class LoggingEvent {
    /**
     * Event emission time in milliseconds.
     */
    public final long millis;
    /**
     * Logging event level.
     */
    public final Level level;
    /**
     * Logger name.
     */
    public final String logger;
    /**
     * Message to log.
     */
    public final String message;
    /**
     * Logging arguments.
     */
    public final Args args;

    /**
     * Constructs new logging event.
     *
     * @param level   Level
     * @param logger  Logger name
     * @param message Message
     * @param rayArgs Arguments collection, obtained from ray
     * @param args    Arguments collection, provided during event creation
     */
    public LoggingEvent(
            final Level level,
            final String logger,
            final String message,
            final Args rayArgs,
            final Arg... args
    ) {
        this.millis = System.currentTimeMillis();
        this.level = Objects.requireNonNull(level, "type");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.message = Objects.requireNonNull(message, "message");
        this.args = LazyMergedArgs.of(rayArgs, args);
    }

    /**
     * Logging event level.
     */
    public enum Level {
        TRACE, DEBUG, INFO, WARNING, ERROR, ALERT, EMERGENCY
    }
}
