package com.github.mono83.events;

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
     * Message to log
     */
    public final String message;
    /**
     * Logging arguments
     */
    public final Args args;

    /**
     * Constructs new logging event.
     *
     * @param level   Level
     * @param message Message
     * @param rayArgs
     * @param args
     */
    public LoggingEvent(
            final Level level,
            final String message,
            final Args rayArgs,
            final Arg... args
    ) {
        this.millis = System.currentTimeMillis();
        this.level = Objects.requireNonNull(level, "type");
        this.message = Objects.requireNonNull(message, "message");
        this.args = null; // TODO lazy args merging
    }

    public enum Level {
        TRACE, DEBUG, INFO, WARNING, ERROR, ALERT, EMERGENCY;
    }
}
