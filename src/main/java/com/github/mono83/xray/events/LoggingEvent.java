package com.github.mono83.xray.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.ArgException;
import com.github.mono83.xray.Args;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiFunction;

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
     * Message pattern to log.
     */
    public final String pattern;
    /**
     * Logging arguments.
     */
    public final Args args;

    /**
     * Constructs new logging event.
     *
     * @param level   Level
     * @param logger  Logger name
     * @param pattern Message pattern
     * @param rayArgs Arguments collection, obtained from ray
     * @param args    Arguments collection, provided during event creation
     */
    public LoggingEvent(
            final Level level,
            final String logger,
            final String pattern,
            final Args rayArgs,
            final Arg... args
    ) {
        this.millis = System.currentTimeMillis();
        this.level = Objects.requireNonNull(level, "type");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.pattern = Objects.requireNonNull(pattern, "pattern");
        this.args = LazyMergedArgs.of(rayArgs, args);
    }

    /**
     * Builds event message using provided placeholder replacer function.
     *
     * @param replacer Placeholder replacer
     * @return Message with placeholders replaced
     */
    public String getMessage(final BiFunction<String, Args, String> replacer) {
        return Objects.requireNonNull(replacer, "replacer").apply(pattern, args);
    }

    /**
     * @return Arguments, that contains exceptions
     */
    public Collection<ArgException> getExceptions() {
        // In most cases there will be no errors
        ArrayList<ArgException> errors = null;
        for (Arg arg : args) {
            if (arg instanceof ArgException) {
                if (errors == null) {
                    errors = new ArrayList<>();
                }
                errors.add((ArgException) arg);
            }
        }

        return errors == null ? Collections.emptyList() : errors;
    }

    /**
     * Logging event level.
     */
    public enum Level {
        TRACE, DEBUG, INFO, WARNING, ERROR, ALERT, EMERGENCY
    }
}
