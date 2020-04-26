package com.github.mono83.xray.events.consumers;

import com.github.mono83.xray.Args;
import com.github.mono83.xray.ReplacerColon;
import com.github.mono83.xray.events.LoggingEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Log events consumer, that outputs it to {@link OutputStream}.
 * <p>
 * Works in synchronous mode.
 */
public class ConsumerToOutputStream implements Consumer<LoggingEvent> {
    /**
     * Output stream to write data into.
     */
    private final OutputStream os;
    /**
     * Placeholder replacement function.
     */
    private final BiFunction<String, Args, String> replacer;
    /**
     * Charset to convert into byte array.
     */
    private final Charset charset;
    /**
     * For time formatting.
     */
    private final SimpleDateFormat timeFormat;

    /**
     * Constructs log events consumer, that will print all data to STD OUT.
     */
    public ConsumerToOutputStream() {
        this(System.out, new ReplacerColon(true));
    }

    /**
     * Constructs log events consumer, that will output logging events to configured
     * {@link OutputStream}.
     *
     * @param os       Output stream to write data into
     * @param replacer Placeholder replacement function.
     */
    public ConsumerToOutputStream(
            final OutputStream os,
            final BiFunction<String, Args, String> replacer
    ) {
        this.os = Objects.requireNonNull(os, "os");
        this.replacer = Objects.requireNonNull(replacer, "replacer");
        this.charset = Charset.forName("UTF-8");
        this.timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        this.timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void accept(final LoggingEvent event) {
        if (event == null) {
            return;
        }

        // Building message
        StringBuilder sb = new StringBuilder();

        // Appending time
        synchronized (timeFormat) {
            sb.append(timeFormat.format(new Date(event.millis)));
        }

        // Appending level
        switch (event.level) {
            case TRACE:
                sb.append(" [TRC] ");
                break;
            case DEBUG:
                sb.append(" [DBG] ");
                break;
            case INFO:
                sb.append(" [INF] ");
                break;
            case WARNING:
                sb.append(" [WRN] ");
                break;
            case ERROR:
                sb.append(" [ERR] ");
                break;
            case ALERT:
                sb.append(" [ALR] ");
                break;
            case EMERGENCY:
                sb.append(" [EMR] ");
                break;
            default:
                sb.append("       ");
        }

        // Appending message
        sb.append(event.getMessage(replacer));

        // Appending logger name
        sb.append(" @").append(event.logger);
        sb.append('\n');


        // Sending to output
        try {
            os.write(sb.toString().getBytes(charset));
        } catch (IOException ignore) {
            // Exception suppressed
        }
    }

    @Override
    public String toString() {
        return "[To OutputStream]";
    }
}
