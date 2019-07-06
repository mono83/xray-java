package com.github.mono83.xray;

/**
 * Describes components, able to process logging data.
 */
public interface ReporterLogs {
    /**
     * Reports message with level TRACE.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void trace(String message, Arg... args);

    /**
     * Reports message with level DEBUG.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void debug(String message, Arg... args);

    /**
     * Reports message with level INFO.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void info(String message, Arg... args);

    /**
     * Reports message with level WARNING.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void warning(String message, Arg... args);

    /**
     * Reports message with level ERROR.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void error(String message, Arg... args);

    /**
     * Reports message with level ALERT.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void alert(String message, Arg... args);

    /**
     * Reports message with level EMERGENCY.
     *
     * @param message Message to report
     * @param args    Logging arguments, optional
     */
    void emergency(String message, Arg... args);
}
