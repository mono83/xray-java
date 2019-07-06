package com.github.mono83.xray;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Base interface to data tracking.
 */
public interface Ray extends ReporterLogs, ReporterMetrics {
    /**
     * @return New child ray
     */
    Ray fork();

    /**
     * Clones ray on same level with new logger name.
     *
     * @param name Logger name
     * @return New ray
     */
    Ray with(String name);

    /**
     * Clones ray on same level with new logger name.
     *
     * @param nameSupplier Logger name supplier
     * @return New ray
     */
    default Ray with(final Supplier<String> nameSupplier) {
        Objects.requireNonNull(nameSupplier);
        return this.with(nameSupplier.get());
    }

    /**
     * Clones ray on same level with new logger name.
     *
     * @param logger Logger, name will be taken from class name
     * @return New ray
     */
    default Ray with(final Object logger) {
        Objects.requireNonNull(logger);
        return this.with(
                logger instanceof Class
                        ? ((Class) logger).getName()
                        : logger.getClass().getName()
        );
    }

    /**
     * Clones ray on same level with new logger name
     * and metric prefix.
     *
     * @param name         Logger name
     * @param metricPrefix Metric prefix
     * @return New ray
     */
    Ray with(String name, String metricPrefix);

    /**
     * Clones ray on same level with new logger name
     * and metric prefix.
     *
     * @param nameSupplier Logger name supplier
     * @param metricPrefix Metric prefix
     * @return New ray
     */
    default Ray with(final Supplier<String> nameSupplier, String metricPrefix) {
        Objects.requireNonNull(nameSupplier);
        return this.with(nameSupplier.get(), metricPrefix);
    }

    /**
     * Clones ray on same level with new logger name
     * and metric prefix.
     *
     * @param logger       Logger, name will be taken from class name
     * @param metricPrefix Metric prefix
     * @return New ray
     */
    default Ray with(final Object logger, String metricPrefix) {
        Objects.requireNonNull(logger);
        return this.with(
                logger instanceof Class
                        ? ((Class) logger).getName()
                        : logger.getClass().getName(),
                metricPrefix
        );
    }

    /**
     * Clones ray on same level with provided
     * logging arguments.
     *
     * @param args Logger argument
     * @return New ray
     */
    Ray with(Arg... args);
}
